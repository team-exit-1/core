package team.exit_1.repo.backend.core.service.domain.game.service

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.exit_1.repo.backend.core.service.domain.game.data.dto.response.QuestionTypeDto
import team.exit_1.repo.backend.core.service.domain.game.data.dto.response.QuizAttemptDetail
import team.exit_1.repo.backend.core.service.domain.game.data.dto.response.QuizDetailInAttempt
import team.exit_1.repo.backend.core.service.domain.game.data.dto.response.QuizOption
import team.exit_1.repo.backend.core.service.domain.game.data.repository.QuizAttemptJpaRepository
import team.exit_1.repo.backend.core.service.global.common.error.exception.ExpectedException
import team.exit_1.repo.backend.core.service.global.config.MockDataConfig

@Service
class QueryIncorrectAttemptsService(
    private val quizAttemptJpaRepository: QuizAttemptJpaRepository,
    private val objectMapper: ObjectMapper,
) {
    @Transactional(readOnly = true)
    fun execute(): List<QuizAttemptDetail> {
        val userId = MockDataConfig.MOCK_USER_ID
        val pageable = PageRequest.of(0, 10)

        val incorrectAttempts = quizAttemptJpaRepository.findIncorrectAttemptsByUserId(userId, pageable)

        return incorrectAttempts.mapIndexed { index, attempt ->
            val quiz =
                attempt.quiz
                    ?: throw ExpectedException(message = "퀴즈 정보가 존재하지 않습니다.", statusCode = HttpStatus.INTERNAL_SERVER_ERROR)

            val quizOptions =
                quiz.options?.let { optionsJson ->
                    val type = object : TypeReference<List<Map<String, String>>>() {}
                    objectMapper.readValue(optionsJson, type).map { option ->
                        QuizOption(
                            id = option["id"] ?: "",
                            text = option["text"] ?: "",
                        )
                    }
                }

            QuizAttemptDetail(
                attemptId = attempt.id ?: 0L,
                attemptOrder = index + 1,
                quiz =
                    QuizDetailInAttempt(
                        quizId = quiz.id ?: 0L,
                        questionType = quiz.questionType?.let { QuestionTypeDto.from(it) },
                        question = quiz.question ?: "",
                        options = quizOptions,
                        difficulty = quiz.difficulty,
                        topic = quiz.topic,
                        basedOnConversation = quiz.basedOnConversation,
                        category = quiz.category,
                        hint = quiz.hint,
                    ),
                userAnswer = attempt.userAnswer ?: "",
                correctAnswer = quiz.correctAnswer ?: "",
                isCorrect = attempt.isCorrect,
                score = attempt.score,
                attemptTime =
                    attempt.attemptTime ?: throw ExpectedException(
                        message = "시도 시간 정보가 존재하지 않습니다.",
                        statusCode = HttpStatus.INTERNAL_SERVER_ERROR,
                    ),
            )
        }
    }
}
