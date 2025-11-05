package team.exit_1.repo.backend.core.service.domain.game.service

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.exit_1.repo.backend.core.service.domain.game.data.dto.response.GameProgressResponse
import team.exit_1.repo.backend.core.service.domain.game.data.repository.GameSessionJpaRepository
import team.exit_1.repo.backend.core.service.domain.game.data.repository.QuizAttemptJpaRepository
import team.exit_1.repo.backend.core.service.global.common.error.exception.ExpectedException

@Service
class QueryGameProgressService(
    private val gameSessionJpaRepository: GameSessionJpaRepository,
    private val quizAttemptJpaRepository: QuizAttemptJpaRepository
) {
    @Transactional(readOnly = true)
    fun execute(sessionId: String): GameProgressResponse {
        val gameSession = gameSessionJpaRepository.findById(sessionId)
            .orElseThrow { ExpectedException(message = "게임 세션이 존재하지 않습니다.", statusCode = HttpStatus.NOT_FOUND) }

        val attempts = quizAttemptJpaRepository.findAllByGameSession(gameSession)
        val totalAttempts = attempts.size.toLong()
        val correctAnswers = attempts.count { it.isCorrect }.toLong()
        val accuracyRate = if (totalAttempts > 0) {
            (correctAnswers.toDouble() / totalAttempts.toDouble()) * 100
        } else {
            0.0
        }

        return GameProgressResponse(
            sessionId = gameSession.id!!,
            totalScore = gameSession.totalScore,
            totalAttempts = totalAttempts,
            correctAnswers = correctAnswers,
            accuracyRate = accuracyRate
        )
    }
}
