package team.exit_1.repo.backend.core.service.domain.service

import org.springframework.stereotype.Service
import team.exit_1.repo.backend.core.service.domain.data.dto.response.CreateConversationResponse
import team.exit_1.repo.backend.core.service.domain.data.entity.Conversation
import team.exit_1.repo.backend.core.service.domain.data.repository.ConversationJpaRepository
import team.exit_1.repo.backend.core.service.global.config.MockDataConfig.Companion.MOCK_USER_ID
import java.time.LocalDateTime
import java.util.*

@Service
class CreateConversationService(
    private val conversationJpaRepository: ConversationJpaRepository
) {
    fun execute(): CreateConversationResponse {
        val conversationId = "conv_${UUID.randomUUID()}"

        val conversation = conversationJpaRepository.save(
            Conversation().apply {
                this.userId = MOCK_USER_ID
                this.id = conversationId
                this.timestamp = LocalDateTime.now()
            }
        )
        return CreateConversationResponse(
            conversationId = conversation.id!!,
            userId = conversation.userId!!,
            timestamp = conversation.timestamp!!
        )
    }
}