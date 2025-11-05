package team.exit_1.repo.backend.core.service.domain.service

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import team.exit_1.repo.backend.core.service.domain.data.dto.response.CreateConversationResponse
import team.exit_1.repo.backend.core.service.domain.data.entity.Conversation
import team.exit_1.repo.backend.core.service.domain.data.repository.ConversationJpaRepository
import team.exit_1.repo.backend.core.service.global.common.error.exception.ExpectedException
import team.exit_1.repo.backend.core.service.global.config.MockDataConfig.Companion.MOCK_CONVERSATION_ID
import team.exit_1.repo.backend.core.service.global.config.MockDataConfig.Companion.MOCK_USER_ID
import java.time.LocalDateTime

@Service
class CreateConversationService(
    private val conversationJpaRepository: ConversationJpaRepository
) {
    fun execute(): CreateConversationResponse {
        // 해커톤용 임시 코드
        existConversationCheck(MOCK_CONVERSATION_ID)

        val conversation = conversationJpaRepository.save(
            Conversation().apply {
                this.userId = MOCK_USER_ID
                this.id = MOCK_CONVERSATION_ID
                this.timestamp = LocalDateTime.now()
            }
        )
        return CreateConversationResponse(
            conversationId = conversation.id!!,
            userId = conversation.userId!!,
            timestamp = conversation.timestamp!!
        )
    }

    private fun existConversationCheck(conversationId: String) {
        val isExist = conversationJpaRepository.existsById(conversationId)
        if (isExist) {
            throw ExpectedException(message = "이미 대화가 존재합니다.", statusCode = HttpStatus.CONFLICT)
        }
    }
}