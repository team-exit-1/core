package team.exit_1.repo.backend.core.service.domain.game.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.exit_1.repo.backend.core.service.domain.game.data.constant.QuizDifficulty
import team.exit_1.repo.backend.core.service.domain.game.data.dto.request.SubmitQuizAnswerRequest
import team.exit_1.repo.backend.core.service.domain.game.data.dto.response.QuizAttemptResponse
import team.exit_1.repo.backend.core.service.domain.game.data.entity.GameSession
import team.exit_1.repo.backend.core.service.domain.game.data.entity.QuizAttempt
import team.exit_1.repo.backend.core.service.domain.game.data.repository.GameSessionJpaRepository
import team.exit_1.repo.backend.core.service.domain.game.data.repository.QuizAttemptJpaRepository
import team.exit_1.repo.backend.core.service.domain.game.data.repository.QuizJpaRepository
import team.exit_1.repo.backend.core.service.global.common.error.exception.ExpectedException
import team.exit_1.repo.backend.core.service.global.config.logger
import team.exit_1.repo.backend.core.service.global.thirdparty.client.LlmServiceClient
import team.exit_1.repo.backend.core.service.global.thirdparty.data.request.GameResultRequest
import team.exit_1.repo.backend.core.service.global.thirdparty.data.response.GameResultResponse
import java.time.LocalDateTime

@Service
class SubmitQuizAnswerService(
    private val gameSessionJpaRepository: GameSessionJpaRepository,
    private val quizJpaRepository: QuizJpaRepository,
    private val quizAttemptJpaRepository: QuizAttemptJpaRepository,
    private val llmServiceClient: LlmServiceClient,
    private val objectMapper: ObjectMapper
) {
    @Transactional
    fun execute(sessionId: String, request: SubmitQuizAnswerRequest): QuizAttemptResponse {
        val gameSession = gameSessionJpaRepository.findById(sessionId)
            .orElseThrow { ExpectedException(message = "게임 세션이 존재하지 않습니다.", statusCode = HttpStatus.NOT_FOUND) }

        val quiz = quizJpaRepository.findById(request.quizId)
            .orElseThrow { ExpectedException(message = "퀴즈가 존재하지 않습니다.", statusCode = HttpStatus.NOT_FOUND) }

        // 이미 제출된 퀴즈인지 확인
        if (quizAttemptJpaRepository.existsByGameSessionAndQuiz(gameSession, quiz)) {
            throw ExpectedException(
                message = "이미 답변을 제출한 퀴즈입니다.",
                statusCode = HttpStatus.CONFLICT
            )
        }

        val userId = gameSession.userId
            ?: throw ExpectedException(message = "사용자 정보가 존재하지 않습니다.", statusCode = HttpStatus.NOT_FOUND)

        // 정답 확인 (대소문자 구분 없이, 공백 제거)
        val isCorrect = quiz.correctAnswer?.trim()?.equals(request.answer.trim(), ignoreCase = true) ?: false

        // 점수 계산 (난이도별 점수)
        val score = if (isCorrect) {
            when (quiz.difficulty) {
                QuizDifficulty.EASY -> 10
                QuizDifficulty.MEDIUM -> 20
                QuizDifficulty.HARD -> 30
            }
        } else {
            0
        }

        // 시도 기록 저장
        val quizAttempt = QuizAttempt().apply {
            this.gameSession = gameSession
            this.quiz = quiz
            this.userAnswer = request.answer
            this.isCorrect = isCorrect
            this.score = score
            this.attemptTime = LocalDateTime.now()
        }

        val savedAttempt = quizAttemptJpaRepository.save(quizAttempt)

        // 게임 세션 점수 업데이트
        gameSession.totalScore += score

        // LLM 서버에 결과 전송 및 평가 받기
        try {
            if (quiz.llmQuestionId != null) {
                val llmResponse = llmServiceClient.evaluateGameResult(
                    GameResultRequest(
                        userId = userId,
                        questionId = quiz.llmQuestionId!!,
                        userAnswer = request.answer,
                        isCorrect = isCorrect,
                        gameSessionId = sessionId
                    )
                )

                if (llmResponse.success && llmResponse.data != null) {
                    val resultResponse = objectMapper.convertValue(llmResponse.data, GameResultResponse::class.java)

                    logger().info(
                        "게임 결과 평가 완료 - Topic: ${resultResponse.memoryEvaluation.topic}, " +
                        "RetentionScore: ${resultResponse.memoryEvaluation.retentionScore}, " +
                        "Recommendation: ${resultResponse.memoryEvaluation.recommendation}"
                    )

                    // LLM 제안에 따라 난이도 조절
                    updateDifficulty(gameSession, resultResponse.nextQuestionSuggestion.difficulty)
                } else {
                    logger().warn("LLM 서버 평가 실패: ${llmResponse.error?.message}. 현재 난이도 유지")
                }
            } else {
                logger().info("LLM QuestionId가 없어 난이도 유지")
            }
        } catch (e: Exception) {
            logger().error("LLM 서버 평가 중 오류 발생. 현재 난이도 유지", e)
        }

        gameSessionJpaRepository.save(gameSession)

        return QuizAttemptResponse(
            attemptId = savedAttempt.id!!,
            quizId = quiz.id!!,
            userAnswer = savedAttempt.userAnswer!!,
            isCorrect = savedAttempt.isCorrect,
            score = savedAttempt.score,
            attemptTime = savedAttempt.attemptTime!!
        )
    }

    private fun updateDifficulty(gameSession: GameSession, suggestedDifficulty: String) {
        val newDifficulty = QuizDifficulty.fromString(suggestedDifficulty)
        if (newDifficulty == null) {
            logger().warn("알 수 없는 난이도 값: $suggestedDifficulty. 현재 난이도 유지")
            return
        }

        if (newDifficulty != gameSession.currentDifficulty) {
            logger().info("난이도 변경: ${gameSession.currentDifficulty} -> $newDifficulty (LLM 제안)")
            gameSession.currentDifficulty = newDifficulty
        }
    }
}
