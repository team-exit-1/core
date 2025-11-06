package team.exit_1.repo.backend.core.service.domain.message.event

import org.springframework.context.ApplicationEvent

data class MessageSentEvent(
    val conversationId: String,
    val messageId: Long,
    val userId: String,
    val content: String
) : ApplicationEvent(conversationId)
