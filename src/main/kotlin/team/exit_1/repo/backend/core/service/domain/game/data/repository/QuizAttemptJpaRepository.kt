package team.exit_1.repo.backend.core.service.domain.game.data.repository

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import team.exit_1.repo.backend.core.service.domain.game.data.entity.GameSession
import team.exit_1.repo.backend.core.service.domain.game.data.entity.Quiz
import team.exit_1.repo.backend.core.service.domain.game.data.entity.QuizAttempt

@Repository
interface QuizAttemptJpaRepository : JpaRepository<QuizAttempt, Long> {
    fun findAllByGameSession(gameSession: GameSession): List<QuizAttempt>

    fun countByGameSession(gameSession: GameSession): Long

    fun existsByGameSessionAndQuiz(
        gameSession: GameSession,
        quiz: Quiz,
    ): Boolean

    @Query(
        """
        SELECT qa FROM QuizAttempt qa
        WHERE qa.gameSession.userId = :userId
        AND qa.isCorrect = false
        ORDER BY qa.attemptTime DESC
    """,
    )
    fun findIncorrectAttemptsByUserId(
        userId: String,
        pageable: Pageable,
    ): List<QuizAttempt>
}
