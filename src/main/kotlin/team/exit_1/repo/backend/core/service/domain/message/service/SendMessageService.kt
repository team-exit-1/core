package team.exit_1.repo.backend.core.service.domain.message.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.exit_1.repo.backend.core.service.domain.message.data.constant.ConversationParticipantType
import team.exit_1.repo.backend.core.service.domain.message.data.dto.request.SendMessageRequest
import team.exit_1.repo.backend.core.service.domain.message.data.dto.response.ContextUsageInfo
import team.exit_1.repo.backend.core.service.domain.message.data.dto.response.MessageResponse
import team.exit_1.repo.backend.core.service.domain.message.data.entity.Message
import team.exit_1.repo.backend.core.service.domain.conversation.data.repository.ConversationJpaRepository
import team.exit_1.repo.backend.core.service.domain.message.data.repository.MessageJpaRepository
import team.exit_1.repo.backend.core.service.global.common.error.exception.ExpectedException
import team.exit_1.repo.backend.core.service.global.config.logger
import team.exit_1.repo.backend.core.service.global.thirdparty.client.LlmServiceClient
import team.exit_1.repo.backend.core.service.global.thirdparty.data.request.ChatRequest
import team.exit_1.repo.backend.core.service.global.thirdparty.data.response.ChatResponse
import java.time.LocalDateTime

@Service
class SendMessageService(
    private val conversationJpaRepository: ConversationJpaRepository,
    private val messageJpaRepository: MessageJpaRepository,
    private val llmServiceClient: LlmServiceClient,
    private val objectMapper: ObjectMapper
) {
    @Transactional
    fun execute(conversationId: String, request: SendMessageRequest): MessageResponse {
        val conversation = conversationJpaRepository.findById(conversationId)
            .orElseThrow { ExpectedException(message = "대화가 존재하지 않습니다.", statusCode = HttpStatus.NOT_FOUND) }

        val userId = conversation.userId
            ?: throw ExpectedException(message = "사용자 정보가 존재하지 않습니다.", statusCode = HttpStatus.INTERNAL_SERVER_ERROR)

        val userMessage = Message().apply {
            this.conversationId = conversation
            this.content = request.content
            this.role = ConversationParticipantType.USER
            this.timestamp = LocalDateTime.now()
        }

        val savedUserMessage = messageJpaRepository.save(userMessage)
        logger().info("사용자 메시지 저장 완료 - messageId: ${savedUserMessage.id}, conversationId: $conversationId")

        val chatRequest = ChatRequest(
            userId = userId,
            message = request.content
        )

        val llmResponse = llmServiceClient.sendChatMessage(chatRequest)

        if (!llmResponse.success || llmResponse.data == null) {
            throw ExpectedException(
                message = "LLM 서버 응답 실패: ${llmResponse.error?.message ?: "알 수 없는 오류"}",
                statusCode = HttpStatus.INTERNAL_SERVER_ERROR
            )
        }

        val chatResponse = objectMapper.convertValue(llmResponse.data, ChatResponse::class.java)

        val aiMessage = Message().apply {
            this.conversationId = conversation
            this.content = chatResponse.response
            this.role = ConversationParticipantType.ASSISTANT
            this.timestamp = LocalDateTime.now()
        }

        val savedAiMessage = messageJpaRepository.save(aiMessage)
        logger().info("AI 응답 메시지 저장 완료 - messageId: ${savedAiMessage.id}, conversationId: $conversationId")

        return MessageResponse(
            messageId = savedAiMessage.id!!,
            content = savedAiMessage.content,
            timestamp = savedAiMessage.timestamp!!,
            contextUsed = chatResponse.contextUsed?.let {
                ContextUsageInfo(
                    totalConversations = it.totalConversations,
                    topScore = it.topScore
                )
            }
        )
    }
}
