package team.exit_1.repo.backend.core.service.domain.conversation.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.exit_1.repo.backend.core.service.domain.conversation.data.dto.response.ConversationResponse
import team.exit_1.repo.backend.core.service.domain.conversation.data.entity.Conversation
import team.exit_1.repo.backend.core.service.domain.conversation.data.repository.ConversationJpaRepository
import team.exit_1.repo.backend.core.service.domain.message.data.constant.ConversationParticipantType
import team.exit_1.repo.backend.core.service.domain.message.data.entity.Message
import team.exit_1.repo.backend.core.service.domain.message.data.repository.MessageJpaRepository
import team.exit_1.repo.backend.core.service.global.common.error.exception.ExpectedException
import team.exit_1.repo.backend.core.service.global.config.MockDataConfig.Companion.MOCK_USER_ID
import team.exit_1.repo.backend.core.service.global.config.logger
import team.exit_1.repo.backend.core.service.global.thirdparty.client.LlmServiceClient
import team.exit_1.repo.backend.core.service.global.thirdparty.data.request.ChatRequest
import team.exit_1.repo.backend.core.service.global.thirdparty.data.response.ChatResponse
import java.time.LocalDateTime
import java.util.*

@Service
class CreateConversationService(
    private val conversationJpaRepository: ConversationJpaRepository,
    private val messageJpaRepository: MessageJpaRepository,
    private val llmServiceClient: LlmServiceClient,
    private val objectMapper: ObjectMapper
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

        val greetingRequest = ChatRequest(
            userId = MOCK_USER_ID,
            message = "인삿말과 함께 이전 대화를 상기할 수 있는 말을 해줘"
        )

        logger().info("대화 시작 인사말 요청 - conversationId: $conversationId, userId: $MOCK_USER_ID")

        val llmResponse = llmServiceClient.sendChatMessage(greetingRequest)

        if (!llmResponse.success || llmResponse.data == null) {
            throw ExpectedException(
                message = "LLM 서버 인사말 응답 실패: ${llmResponse.error?.message ?: "알 수 없는 오류"}",
                statusCode = HttpStatus.INTERNAL_SERVER_ERROR
            )
        }

        val chatResponse = objectMapper.convertValue(llmResponse.data, ChatResponse::class.java)
        val greetingMessage = chatResponse.response

        val aiGreetingMessage = Message().apply {
            this.conversationId = conversation
            this.content = greetingMessage
            this.role = ConversationParticipantType.ASSISTANT
            this.timestamp = LocalDateTime.now()
        }

        messageJpaRepository.save(aiGreetingMessage)
        logger().info("AI 인사말 메시지 저장 완료 - conversationId: $conversationId")

        return ConversationResponse(
            conversationId = conversation.id!!,
            userId = conversation.userId!!,
            conversationStatus = conversation.status,
            timestamp = conversation.timestamp!!,
            initialGreeting = greetingMessage
        )
    }
}