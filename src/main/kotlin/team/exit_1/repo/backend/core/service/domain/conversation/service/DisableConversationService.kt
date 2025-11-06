package team.exit_1.repo.backend.core.service.domain.conversation.service

import org.springframework.context.ApplicationEventPublisher
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.exit_1.repo.backend.core.service.domain.conversation.data.constant.ConversationStatus
import team.exit_1.repo.backend.core.service.domain.conversation.data.dto.response.ConversationResponse
import team.exit_1.repo.backend.core.service.domain.conversation.data.repository.ConversationJpaRepository
import team.exit_1.repo.backend.core.service.domain.conversation.event.ConversationDisabledEvent
import team.exit_1.repo.backend.core.service.global.common.error.exception.ExpectedException

@Service
class DisableConversationService(
    private val conversationRepository: ConversationJpaRepository,
    private val eventPublisher: ApplicationEventPublisher,
) {
    @Transactional
    fun execute(conversationId: String): ConversationResponse {
        val conversation =
            conversationRepository.findById(conversationId).orElseThrow {
                ExpectedException(message = "대화가 존재하지 않습니다.", HttpStatus.NOT_FOUND)
            }
        conversation.status = ConversationStatus.DISABLED
        val updatedConversation = conversationRepository.save(conversation)

        eventPublisher.publishEvent(
            ConversationDisabledEvent(
                source = this,
                conversation = updatedConversation,
                conversationId = conversationId,
            ),
        )

        return ConversationResponse(
            conversationId = updatedConversation.id!!,
            userId = updatedConversation.userId!!,
            conversationStatus = updatedConversation.status,
            timestamp = updatedConversation.timestamp!!,
            initialGreeting = null,
        )
    }
}
