package team.exit_1.repo.backend.core.service.domain.message.service

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.exit_1.repo.backend.core.service.domain.message.data.constant.ConversationParticipantType
import team.exit_1.repo.backend.core.service.domain.message.data.dto.request.SendMessageRequest
import team.exit_1.repo.backend.core.service.domain.message.data.dto.response.MessageResponse
import team.exit_1.repo.backend.core.service.domain.message.data.entity.Message
import team.exit_1.repo.backend.core.service.domain.conversation.data.repository.ConversationJpaRepository
import team.exit_1.repo.backend.core.service.domain.message.data.repository.MessageJpaRepository
import team.exit_1.repo.backend.core.service.global.common.error.exception.ExpectedException
import java.time.LocalDateTime

@Service
class SendMessageService(
    private val conversationJpaRepository: ConversationJpaRepository,
    private val messageJpaRepository: MessageJpaRepository
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

        // TODO: 메시지 전송 완료 후 이벤트 발행 (추후 구현)

        return MessageResponse(
            messageId = savedMessage.id!!,
            content = savedMessage.content,
            role = savedMessage.role!!,
            timestamp = savedMessage.timestamp!!
        )
    }
}
