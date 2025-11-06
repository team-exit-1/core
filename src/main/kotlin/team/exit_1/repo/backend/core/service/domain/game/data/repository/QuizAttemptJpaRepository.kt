package team.exit_1.repo.backend.core.service.domain.game.data.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import team.exit_1.repo.backend.core.service.domain.game.data.entity.GameSession
import team.exit_1.repo.backend.core.service.domain.game.data.entity.Quiz
import team.exit_1.repo.backend.core.service.domain.game.data.entity.QuizAttempt

@Repository
interface QuizAttemptJpaRepository : JpaRepository<QuizAttempt, Long> {
    fun findAllByGameSession(gameSession: GameSession): List<QuizAttempt>
    fun countByGameSession(gameSession: GameSession): Long
    fun existsByGameSessionAndQuiz(gameSession: GameSession, quiz: Quiz): Boolean
}
