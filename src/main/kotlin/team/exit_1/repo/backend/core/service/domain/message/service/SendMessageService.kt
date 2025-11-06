package team.exit_1.repo.backend.core.service.domain.message.service

import org.springframework.context.ApplicationEventPublisher
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.exit_1.repo.backend.core.service.domain.message.data.constant.ConversationParticipantType
import team.exit_1.repo.backend.core.service.domain.message.data.dto.request.SendMessageRequest
import team.exit_1.repo.backend.core.service.domain.message.data.dto.response.MessageResponse
import team.exit_1.repo.backend.core.service.domain.message.data.entity.Message
import team.exit_1.repo.backend.core.service.domain.conversation.data.repository.ConversationJpaRepository
import team.exit_1.repo.backend.core.service.domain.message.data.repository.MessageJpaRepository
import team.exit_1.repo.backend.core.service.domain.message.event.MessageSentEvent
import team.exit_1.repo.backend.core.service.global.common.error.exception.ExpectedException
import team.exit_1.repo.backend.core.service.global.config.logger
import java.time.LocalDateTime

@Service
class SendMessageService(
    private val conversationJpaRepository: ConversationJpaRepository,
    private val messageJpaRepository: MessageJpaRepository,
    private val eventPublisher: ApplicationEventPublisher
) {
    @Transactional
    fun execute(conversationId: String, request: SendMessageRequest): MessageResponse {
        val conversation = conversationJpaRepository.findById(conversationId)
            .orElseThrow { ExpectedException(message = "대화가 존재하지 않습니다.", statusCode = HttpStatus.NOT_FOUND) }

        val message = Message().apply {
            this.conversationId = conversation
            this.content = request.content
            this.role = ConversationParticipantType.USER
            this.timestamp = LocalDateTime.now()
        }

        val savedMessage = messageJpaRepository.save(message)

        val userId = conversation.userId
            ?: throw ExpectedException(message = "사용자 정보가 존재하지 않습니다.", statusCode = HttpStatus.INTERNAL_SERVER_ERROR)

        eventPublisher.publishEvent(
            MessageSentEvent(
                conversationId = conversationId,
                messageId = savedMessage.id!!,
                userId = userId,
                content = request.content
            )
        )

        return MessageResponse(
            messageId = savedMessage.id!!,
            content = savedMessage.content,
            role = savedMessage.role!!,
            timestamp = savedMessage.timestamp!!
        )
    }
}
