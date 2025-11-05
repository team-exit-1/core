package team.exit_1.repo.backend.core.service.domain.message.data.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import team.exit_1.repo.backend.core.service.domain.conversation.data.entity.Conversation
import team.exit_1.repo.backend.core.service.domain.message.data.entity.Message

@Repository
interface MessageJpaRepository : JpaRepository<Message, Long> {
    fun findAllByConversationIdOrderByTimestampAsc(conversationId: Conversation): List<Message>
}
