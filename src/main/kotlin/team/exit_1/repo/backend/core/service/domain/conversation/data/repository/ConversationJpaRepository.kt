package team.exit_1.repo.backend.core.service.domain.conversation.data.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import team.exit_1.repo.backend.core.service.domain.conversation.data.constant.ConversationStatus
import team.exit_1.repo.backend.core.service.domain.conversation.data.entity.Conversation

@Repository
interface ConversationJpaRepository : JpaRepository<Conversation, String> {
    fun findAllByStatus(status: ConversationStatus): List<Conversation>
}
