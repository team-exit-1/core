package team.exit_1.repo.backend.core.service.domain.message.service

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.exit_1.repo.backend.core.service.domain.message.data.dto.response.MessageResponse
import team.exit_1.repo.backend.core.service.domain.conversation.data.repository.ConversationJpaRepository
import team.exit_1.repo.backend.core.service.domain.message.data.repository.MessageJpaRepository
import team.exit_1.repo.backend.core.service.global.common.error.exception.ExpectedException

@Service
class QueryMessagesService(
    private val conversationJpaRepository: ConversationJpaRepository,
    private val messageJpaRepository: MessageJpaRepository
) {
    @Transactional(readOnly = true)
    fun execute(conversationId: String): List<MessageResponse> {
        val conversation = conversationJpaRepository.findById(conversationId)
            .orElseThrow { ExpectedException(message = "대화가 존재하지 않습니다.", statusCode = HttpStatus.NOT_FOUND) }

        return messageJpaRepository.findAllByConversationIdOrderByTimestampAsc(conversation)
            .map { message ->
                MessageResponse(
                    messageId = message.id!!,
                    content = message.content,
                    role = message.role!!,
                    timestamp = message.timestamp!!
                )
            }
    }
}
