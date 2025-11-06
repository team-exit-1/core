package team.exit_1.repo.backend.core.service.domain.conversation.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.exit_1.repo.backend.core.service.domain.conversation.data.constant.ConversationStatus
import team.exit_1.repo.backend.core.service.domain.conversation.data.dto.response.ConversationResponse
import team.exit_1.repo.backend.core.service.domain.conversation.data.repository.ConversationJpaRepository

@Service
class QueryActiveConversationsService(
    private val conversationJpaRepository: ConversationJpaRepository,
) {
    @Transactional(readOnly = true)
    fun execute(): List<ConversationResponse> =
        conversationJpaRepository
            .findAllByStatus(ConversationStatus.ACTIVE)
            .map { conversation ->
                ConversationResponse(
                    conversationId = conversation.id!!,
                    userId = conversation.userId!!,
                    timestamp = conversation.timestamp!!,
                    conversationStatus = conversation.status,
                    initialGreeting = null,
                )
            }
}
