package team.exit_1.repo.backend.core.service.domain.game.service

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.exit_1.repo.backend.core.service.domain.game.data.constant.GameSessionStatus
import team.exit_1.repo.backend.core.service.domain.game.data.constant.QuizDifficulty
import team.exit_1.repo.backend.core.service.domain.game.data.dto.response.QuestionTypeDto
import team.exit_1.repo.backend.core.service.domain.game.data.dto.response.QuizOption
import team.exit_1.repo.backend.core.service.domain.game.data.dto.response.QuizResponse
import team.exit_1.repo.backend.core.service.domain.game.data.entity.GameSession
import team.exit_1.repo.backend.core.service.domain.game.data.entity.Quiz
import team.exit_1.repo.backend.core.service.domain.game.data.repository.GameSessionJpaRepository
import team.exit_1.repo.backend.core.service.domain.game.data.repository.QuizAttemptJpaRepository
import team.exit_1.repo.backend.core.service.domain.game.data.repository.QuizJpaRepository
import team.exit_1.repo.backend.core.service.global.common.error.exception.ExpectedException
import team.exit_1.repo.backend.core.service.global.config.MockDataConfig
import team.exit_1.repo.backend.core.service.global.thirdparty.client.LlmServiceClient
import team.exit_1.repo.backend.core.service.global.thirdparty.data.request.GameQuestionRequest
import team.exit_1.repo.backend.core.service.global.thirdparty.data.request.QuestionTypeRequest
import team.exit_1.repo.backend.core.service.global.thirdparty.data.request.QuizDifficultyRequest
import team.exit_1.repo.backend.core.service.global.thirdparty.data.response.GameQuestionResponse
import java.time.LocalDateTime

@Service
class QueryQuizzesService(
    private val gameSessionJpaRepository: GameSessionJpaRepository,
    private val quizJpaRepository: QuizJpaRepository,
    private val quizAttemptJpaRepository: QuizAttemptJpaRepository,
    private val llmServiceClient: LlmServiceClient,
    private val objectMapper: ObjectMapper,
) {
    @Transactional
    fun execute(sessionId: String): List<QuizResponse> {
        val gameSession =
            gameSessionJpaRepository
                .findById(sessionId)
                .orElseThrow { ExpectedException(message = "게임 세션이 존재하지 않습니다.", statusCode = HttpStatus.NOT_FOUND) }

        validateGameSession(gameSession)

        val userId =
            gameSession.userId
                ?: throw ExpectedException(message = "사용자 정보가 존재하지 않습니다.", statusCode = HttpStatus.NOT_FOUND)

        val questionType =
            if (gameSession.currentDifficulty == QuizDifficulty.EASY) {
                QuestionTypeRequest.FILL_IN_BLANK
            } else {
                QuestionTypeRequest.MULTIPLE_CHOICE
            }
        val difficultyHint = QuizDifficultyRequest.from(gameSession.currentDifficulty)

        val llmResponse =
            llmServiceClient.generateGameQuestion(
                GameQuestionRequest(
                    userId = userId,
                    questionType = questionType,
                    difficultyHint = difficultyHint,
                ),
            )

        if (!llmResponse.success || llmResponse.data == null) {
            throw ExpectedException(
                message = "LLM 서버에서 질문 생성에 실패했습니다: ${llmResponse.error?.message}",
                statusCode = HttpStatus.INTERNAL_SERVER_ERROR,
            )
        }

        val questionResponse = objectMapper.convertValue(llmResponse.data, GameQuestionResponse::class.java)

        val parsedQuestionType =
            questionResponse.parseQuestionType()
                ?: throw ExpectedException(
                    message = "LLM 서버에서 알 수 없는 질문 타입을 반환했습니다: ${questionResponse.questionType}",
                    statusCode = HttpStatus.INTERNAL_SERVER_ERROR,
                )

        val quiz =
            Quiz().apply {
                this.llmQuestionId = questionResponse.questionId
                this.questionType = parsedQuestionType
                this.question = questionResponse.question
                this.correctAnswer = questionResponse.correctAnswer
                this.difficulty = gameSession.currentDifficulty
                this.topic = questionResponse.metadata.topic
                this.basedOnConversation = questionResponse.basedOnConversation
                this.memoryScore = questionResponse.metadata.memoryScore
                this.daysSinceConversation = questionResponse.metadata.daysSinceConversation
                this.category = questionResponse.metadata.topic

                if (questionResponse.options != null) {
                    this.options = objectMapper.writeValueAsString(questionResponse.options)
                }
            }

        val savedQuiz = quizJpaRepository.save(quiz)

        // QuizResponse 생성
        val quizOptions =
            savedQuiz.options?.let { optionsJson ->
                val type = object : TypeReference<List<Map<String, String>>>() {}
                objectMapper.readValue(optionsJson, type).map { option ->
                    QuizOption(
                        id = option["id"] ?: "",
                        text = option["text"] ?: "",
                    )
                }
            }

        return listOf(
            QuizResponse(
                quizId = savedQuiz.id!!,
                questionType = savedQuiz.questionType?.let { QuestionTypeDto.from(it) },
                question = savedQuiz.question!!,
                options = quizOptions,
                difficulty = savedQuiz.difficulty,
                topic = savedQuiz.topic,
                basedOnConversation = savedQuiz.basedOnConversation,
                category = savedQuiz.category,
                hint = savedQuiz.hint,
            ),
        )
    }

    private fun validateGameSession(gameSession: GameSession) {
        if (gameSession.status == GameSessionStatus.COMPLETED) {
            throw ExpectedException(
                message = "이미 종료된 게임 세션입니다.",
                statusCode = HttpStatus.CONFLICT,
            )
        }

        val completedQuizCount = quizAttemptJpaRepository.countByGameSession(gameSession)
        val maxQuizCount = MockDataConfig.MAX_QUIZ_COUNT_PER_SESSION

        if (completedQuizCount >= maxQuizCount) {
            gameSession.status = GameSessionStatus.COMPLETED
            gameSession.endTime = LocalDateTime.now()
            gameSessionJpaRepository.save(gameSession)

            throw ExpectedException(
                message = "게임 세션이 최대 퀴즈 개수(${maxQuizCount}개)에 도달하여 자동 종료되었습니다.",
                statusCode = HttpStatus.GONE,
            )
        }

        val startTime =
            gameSession.startTime
                ?: throw ExpectedException(message = "게임 세션 시작 시간이 존재하지 않습니다.", statusCode = HttpStatus.INTERNAL_SERVER_ERROR)

        val now = LocalDateTime.now()
        val timeLimitHours = MockDataConfig.GAME_SESSION_TIME_LIMIT_HOURS
        val expirationTime = startTime.plusHours(timeLimitHours)

        if (now.isAfter(expirationTime)) {
            gameSession.status = GameSessionStatus.COMPLETED
            gameSession.endTime = now
            gameSessionJpaRepository.save(gameSession)

            throw ExpectedException(
                message = "게임 세션이 시간 제한(${timeLimitHours}시간)을 초과하여 자동 종료되었습니다.",
                statusCode = HttpStatus.GONE,
            )
        }
    }
}
