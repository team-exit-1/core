package team.exit_1.repo.backend.core.service.domain.conversation.event

import org.springframework.context.ApplicationEvent
import team.exit_1.repo.backend.core.service.domain.conversation.data.entity.Conversation

class ConversationDisabledEvent(
    source: Any,
    val conversation: Conversation,
    val conversationId: String
) : ApplicationEvent(source)