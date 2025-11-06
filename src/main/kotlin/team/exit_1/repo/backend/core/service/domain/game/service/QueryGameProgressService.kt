package team.exit_1.repo.backend.core.service.domain.game.service

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.exit_1.repo.backend.core.service.domain.game.data.constant.GameSessionStatus
import team.exit_1.repo.backend.core.service.domain.game.data.dto.response.GameProgressResponse
import team.exit_1.repo.backend.core.service.domain.game.data.entity.GameSession
import team.exit_1.repo.backend.core.service.domain.game.data.repository.GameSessionJpaRepository
import team.exit_1.repo.backend.core.service.domain.game.data.repository.QuizAttemptJpaRepository
import team.exit_1.repo.backend.core.service.global.common.error.exception.ExpectedException

@Service
class QueryGameProgressService(
    private val gameSessionJpaRepository: GameSessionJpaRepository,
    private val quizAttemptJpaRepository: QuizAttemptJpaRepository,
) {
    @Transactional(readOnly = true)
    fun execute(sessionId: String): GameProgressResponse {
        val gameSession =
            gameSessionJpaRepository
                .findById(sessionId)
                .orElseThrow { ExpectedException(message = "게임 세션이 존재하지 않습니다.", statusCode = HttpStatus.NOT_FOUND) }

        validateGameSession(gameSession)

        val attempts = quizAttemptJpaRepository.findAllByGameSession(gameSession)
        val totalAttempts = attempts.size.toLong()
        val correctAnswers = attempts.count { it.isCorrect }.toLong()
        val accuracyRate =
            if (totalAttempts > 0) {
                (correctAnswers.toDouble() / totalAttempts.toDouble()) * 100
            } else {
                0.0
            }

        return GameProgressResponse(
            sessionId = gameSession.id!!,
            totalScore = gameSession.totalScore,
            totalAttempts = totalAttempts,
            correctAnswers = correctAnswers,
            accuracyRate = accuracyRate,
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
}
