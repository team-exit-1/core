package team.exit_1.repo.backend.core.service.domain.game.data.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import team.exit_1.repo.backend.core.service.domain.game.data.constant.GameSessionStatus
import team.exit_1.repo.backend.core.service.domain.game.data.entity.GameSession

@Repository
interface GameSessionJpaRepository : JpaRepository<GameSession, String> {
    fun findAllByUserId(userId: String): List<GameSession>
    fun findByUserIdAndStatus(userId: String, status: GameSessionStatus): GameSession?
}
