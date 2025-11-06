package team.exit_1.repo.backend.core.service.domain.game.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.exit_1.repo.backend.core.service.domain.game.data.constant.GameSessionStatus
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
    private val objectMapper: ObjectMapper,
) {
    @Transactional
    fun execute(
        sessionId: String,
        request: SubmitQuizAnswerRequest,
    ): QuizAttemptResponse {
        val gameSession =
            gameSessionJpaRepository
                .findById(sessionId)
                .orElseThrow { ExpectedException(message = "게임 세션이 존재하지 않습니다.", statusCode = HttpStatus.NOT_FOUND) }

        validateGameSession(gameSession)

        val quiz =
            quizJpaRepository
                .findById(request.quizId)
                .orElseThrow { ExpectedException(message = "퀴즈가 존재하지 않습니다.", statusCode = HttpStatus.NOT_FOUND) }

        if (quizAttemptJpaRepository.existsByGameSessionAndQuiz(gameSession, quiz)) {
            throw ExpectedException(
                message = "이미 답변을 제출한 퀴즈입니다.",
                statusCode = HttpStatus.CONFLICT,
            )
        }

        val userId =
            gameSession.userId
                ?: throw ExpectedException(message = "사용자 정보가 존재하지 않습니다.", statusCode = HttpStatus.NOT_FOUND)

        val isCorrect = quiz.correctAnswer?.trim()?.equals(request.answer.trim(), ignoreCase = true) ?: false

        val score =
            if (isCorrect) {
                when (quiz.difficulty) {
                    QuizDifficulty.EASY -> 10
                    QuizDifficulty.MEDIUM -> 20
                    QuizDifficulty.HARD -> 30
                }
            } else {
                0
            }

        val quizAttempt =
            QuizAttempt().apply {
                this.gameSession = gameSession
                this.quiz = quiz
                this.userAnswer = request.answer
                this.isCorrect = isCorrect
                this.score = score
                this.attemptTime = LocalDateTime.now()
            }

        val savedAttempt = quizAttemptJpaRepository.save(quizAttempt)

        gameSession.totalScore += score

        try {
            if (quiz.llmQuestionId != null) {
                val llmResponse =
                    llmServiceClient.evaluateGameResult(
                        GameResultRequest(
                            userId = userId,
                            questionId = quiz.llmQuestionId!!,
                            userAnswer = request.answer,
                            isCorrect = isCorrect,
                            gameSessionId = sessionId,
                        ),
                    )

                if (llmResponse.success && llmResponse.data != null) {
                    val resultResponse = objectMapper.convertValue(llmResponse.data, GameResultResponse::class.java)

                    logger().info(
                        "게임 결과 평가 완료 - Topic: ${resultResponse.memoryEvaluation.topic}, " +
                            "RetentionScore: ${resultResponse.memoryEvaluation.retentionScore}, " +
                            "Recommendation: ${resultResponse.memoryEvaluation.recommendation}",
                    )

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

        // MAX_QUIZ_COUNT_PER_SESSION 제한 비활성화
        // val completedQuizCount = quizAttemptJpaRepository.countByGameSession(gameSession)
        // val maxQuizCount = MockDataConfig.MAX_QUIZ_COUNT_PER_SESSION
        //
        // if (completedQuizCount >= maxQuizCount) {
        //     gameSession.status = GameSessionStatus.COMPLETED
        //     gameSession.endTime = LocalDateTime.now()
        //     logger().info("게임 세션 자동 종료: ${maxQuizCount}개 퀴즈 완료 (sessionId: $sessionId)")
        // }

        gameSessionJpaRepository.save(gameSession)

        return QuizAttemptResponse(
            attemptId = savedAttempt.id!!,
            quizId = quiz.id!!,
            userAnswer = savedAttempt.userAnswer!!,
            isCorrect = savedAttempt.isCorrect,
            score = savedAttempt.score,
            attemptTime = savedAttempt.attemptTime!!,
        )
    }

    private fun validateGameSession(gameSession: GameSession) {
        if (gameSession.status == GameSessionStatus.COMPLETED) {
            throw ExpectedException(
                message = "이미 종료된 게임 세션입니다.",
                statusCode = HttpStatus.CONFLICT,
            )
        }

        // MAX_QUIZ_COUNT_PER_SESSION 제한 비활성화
        // val completedQuizCount = quizAttemptJpaRepository.countByGameSession(gameSession)
        // val maxQuizCount = MockDataConfig.MAX_QUIZ_COUNT_PER_SESSION
        //
        // if (completedQuizCount >= maxQuizCount) {
        //     gameSession.status = GameSessionStatus.COMPLETED
        //     gameSession.endTime = LocalDateTime.now()
        //     gameSessionJpaRepository.save(gameSession)
        //
        //     throw ExpectedException(
        //         message = "게임 세션이 최대 퀴즈 개수(${maxQuizCount}개)에 도달하여 자동 종료되었습니다.",
        //         statusCode = HttpStatus.GONE,
        //     )
        // }

        // GAME_SESSION_TIME_LIMIT_HOURS 제한 비활성화
        // val startTime =
        //     gameSession.startTime
        //         ?: throw ExpectedException(message = "게임 세션 시작 시간이 존재하지 않습니다.", statusCode = HttpStatus.INTERNAL_SERVER_ERROR)
        //
        // val now = LocalDateTime.now()
        // val timeLimitHours = MockDataConfig.GAME_SESSION_TIME_LIMIT_HOURS
        // val expirationTime = startTime.plusHours(timeLimitHours)
        //
        // if (now.isAfter(expirationTime)) {
        //     gameSession.status = GameSessionStatus.COMPLETED
        //     gameSession.endTime = now
        //     gameSessionJpaRepository.save(gameSession)
        //
        //     throw ExpectedException(
        //         message = "게임 세션이 시간 제한(${timeLimitHours}시간)을 초과하여 자동 종료되었습니다.",
        //         statusCode = HttpStatus.GONE,
        //     )
        // }
    }

    private fun updateDifficulty(
        gameSession: GameSession,
        suggestedDifficulty: String,
    ) {
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
