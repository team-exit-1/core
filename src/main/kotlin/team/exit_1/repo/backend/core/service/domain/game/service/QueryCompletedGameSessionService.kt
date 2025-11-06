package team.exit_1.repo.backend.core.service.domain.game.service

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.exit_1.repo.backend.core.service.domain.game.data.constant.GameSessionStatus
import team.exit_1.repo.backend.core.service.domain.game.data.constant.QuizDifficulty
import team.exit_1.repo.backend.core.service.domain.game.data.dto.response.*
import team.exit_1.repo.backend.core.service.domain.game.data.repository.GameSessionJpaRepository
import team.exit_1.repo.backend.core.service.domain.game.data.repository.QuizAttemptJpaRepository
import team.exit_1.repo.backend.core.service.global.common.error.exception.ExpectedException
import java.time.Duration

@Service
class QueryCompletedGameSessionService(
    private val gameSessionJpaRepository: GameSessionJpaRepository,
    private val quizAttemptJpaRepository: QuizAttemptJpaRepository,
    private val objectMapper: ObjectMapper
) {
    @Transactional(readOnly = true)
    fun execute(sessionId: String): CompletedGameSessionDetailResponse {
        val gameSession = gameSessionJpaRepository.findById(sessionId)
            .orElseThrow { ExpectedException(message = "게임 세션이 존재하지 않습니다.", statusCode = HttpStatus.NOT_FOUND) }

        if (gameSession.status != GameSessionStatus.COMPLETED) {
            throw ExpectedException(
                message = "아직 완료되지 않은 게임 세션입니다. 진행 중인 세션은 /progress 엔드포인트를 사용해주세요.",
                statusCode = HttpStatus.BAD_REQUEST
            )
        }

        val userId = gameSession.userId
            ?: throw ExpectedException(message = "사용자 정보가 존재하지 않습니다.", statusCode = HttpStatus.NOT_FOUND)

        val attempts = quizAttemptJpaRepository.findAllByGameSession(gameSession)
            .sortedBy { it.attemptTime }

        val totalQuizzes = attempts.size
        val correctAnswers = attempts.count { it.isCorrect }
        val wrongAnswers = totalQuizzes - correctAnswers
        val accuracyRate = if (totalQuizzes > 0) {
            (correctAnswers.toDouble() / totalQuizzes.toDouble()) * 100
        } else {
            0.0
        }

        val playTimeMinutes = if (gameSession.startTime != null && gameSession.endTime != null) {
            Duration.between(gameSession.startTime, gameSession.endTime).toMinutes()
        } else {
            0L
        }

        val accuracyByDifficulty = calculateAccuracyByDifficulty(attempts)

        val statistics = GameStatistics(
            totalScore = gameSession.totalScore,
            totalQuizzes = totalQuizzes,
            correctAnswers = correctAnswers,
            wrongAnswers = wrongAnswers,
            accuracyRate = accuracyRate,
            playTimeMinutes = playTimeMinutes,
            accuracyByDifficulty = accuracyByDifficulty
        )

        val quizAttemptDetails = attempts.mapIndexed { index, attempt ->
            val quiz = attempt.quiz
                ?: throw ExpectedException(message = "퀴즈 정보가 존재하지 않습니다.", statusCode = HttpStatus.INTERNAL_SERVER_ERROR)

            val quizOptions = quiz.options?.let { optionsJson ->
                val type = object : TypeReference<List<Map<String, String>>>() {}
                objectMapper.readValue(optionsJson, type).map { option ->
                    QuizOption(
                        id = option["id"] ?: "",
                        text = option["text"] ?: ""
                    )
                }
            }

            QuizAttemptDetail(
                attemptId = attempt.id ?: 0L,
                attemptOrder = index + 1,
                quiz = QuizDetailInAttempt(
                    quizId = quiz.id ?: 0L,
                    questionType = quiz.questionType?.let { QuestionTypeDto.from(it) },
                    question = quiz.question ?: "",
                    options = quizOptions,
                    difficulty = quiz.difficulty,
                    topic = quiz.topic,
                    basedOnConversation = quiz.basedOnConversation,
                    category = quiz.category,
                    hint = quiz.hint
                ),
                userAnswer = attempt.userAnswer ?: "",
                correctAnswer = quiz.correctAnswer ?: "",
                isCorrect = attempt.isCorrect,
                score = attempt.score,
                attemptTime = attempt.attemptTime ?: throw ExpectedException(
                    message = "시도 시간 정보가 존재하지 않습니다.",
                    statusCode = HttpStatus.INTERNAL_SERVER_ERROR
                )
            )
        }

        return CompletedGameSessionDetailResponse(
            sessionId = gameSession.id!!,
            userId = userId,
            status = gameSession.status,
            startTime = gameSession.startTime ?: throw ExpectedException(
                message = "세션 시작 시간 정보가 존재하지 않습니다.",
                statusCode = HttpStatus.INTERNAL_SERVER_ERROR
            ),
            endTime = gameSession.endTime,
            finalDifficulty = gameSession.currentDifficulty,
            statistics = statistics,
            quizAttempts = quizAttemptDetails
        )
    }

    private fun calculateAccuracyByDifficulty(attempts: List<team.exit_1.repo.backend.core.service.domain.game.data.entity.QuizAttempt>): Map<QuizDifficulty, DifficultyStatistics> {
        return attempts
            .groupBy { it.quiz?.difficulty ?: QuizDifficulty.EASY }
            .mapValues { (_, attemptsForDifficulty) ->
                val total = attemptsForDifficulty.size
                val correct = attemptsForDifficulty.count { it.isCorrect }
                val accuracy = if (total > 0) {
                    (correct.toDouble() / total.toDouble()) * 100
                } else {
                    0.0
                }

                DifficultyStatistics(
                    total = total,
                    correct = correct,
                    accuracyRate = accuracy
                )
            }
    }
}

