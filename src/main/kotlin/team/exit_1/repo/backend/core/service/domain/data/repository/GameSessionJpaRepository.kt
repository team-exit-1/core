package team.exit_1.repo.backend.core.service.domain.data.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import team.exit_1.repo.backend.core.service.domain.data.constant.GameSessionStatus
import team.exit_1.repo.backend.core.service.domain.data.entity.Conversation
import team.exit_1.repo.backend.core.service.domain.data.entity.GameSession

@Repository
interface GameSessionJpaRepository : JpaRepository<GameSession, String> {
    fun findAllByConversation(conversation: Conversation): List<GameSession>
    fun findByConversationAndStatus(conversation: Conversation, status: GameSessionStatus): GameSession?
}
