package team.exit_1.repo.backend.core.service.domain.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.exit_1.repo.backend.core.service.domain.data.dto.response.ConversationResponse
import team.exit_1.repo.backend.core.service.domain.data.entity.Conversation
import team.exit_1.repo.backend.core.service.domain.data.repository.ConversationJpaRepository
import team.exit_1.repo.backend.core.service.global.config.MockDataConfig.Companion.MOCK_USER_ID
import java.time.LocalDateTime
import java.util.*

@Service
class CreateConversationService(
    private val conversationJpaRepository: ConversationJpaRepository
) {
    @Transactional
    fun execute(): ConversationResponse {
        val conversationId = "conv_${UUID.randomUUID()}"

        val conversation = conversationJpaRepository.save(
            Conversation().apply {
                this.userId = MOCK_USER_ID
                this.id = conversationId
                this.timestamp = LocalDateTime.now()
            }
        )
        return ConversationResponse(
            conversationId = conversation.id!!,
            userId = conversation.userId!!,
            conversationStatus = conversation.status,
            timestamp = conversation.timestamp!!
        )
    }
}