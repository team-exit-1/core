package team.exit_1.repo.backend.core.service.domain.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.exit_1.repo.backend.core.service.domain.data.constant.ConversationStatus
import team.exit_1.repo.backend.core.service.domain.data.dto.response.ConversationResponse
import team.exit_1.repo.backend.core.service.domain.data.repository.ConversationJpaRepository

@Service
class QueryActiveConversationsService(
    private val conversationJpaRepository: ConversationJpaRepository
) {
    @Transactional(readOnly = true)
    fun execute(): List<ConversationResponse> {
        return conversationJpaRepository.findAllByStatus(ConversationStatus.ACTIVE)
            .map { conversation ->
                ConversationResponse(
                    conversationId = conversation.id!!,
                    userId = conversation.userId!!,
                    timestamp = conversation.timestamp!!,
                    conversationStatus = conversation.status
                )
            }
    }
}