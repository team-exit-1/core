package team.exit_1.repo.backend.core.service.domain.message.listener

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import team.exit_1.repo.backend.core.service.domain.conversation.data.repository.ConversationJpaRepository
import team.exit_1.repo.backend.core.service.domain.message.data.constant.ConversationParticipantType
import team.exit_1.repo.backend.core.service.domain.message.data.entity.Message
import team.exit_1.repo.backend.core.service.domain.message.data.repository.MessageJpaRepository
import team.exit_1.repo.backend.core.service.domain.message.event.MessageSentEvent
import team.exit_1.repo.backend.core.service.global.config.logger
import team.exit_1.repo.backend.core.service.global.thirdparty.client.LlmServiceClient
import team.exit_1.repo.backend.core.service.global.thirdparty.data.request.ChatRequest
import team.exit_1.repo.backend.core.service.global.thirdparty.data.response.ChatResponse
import java.time.LocalDateTime

// 동기식 처리로 변경되어 더 이상 사용하지 않음
// @Component
open class MessageSentEventListener(
    private val llmServiceClient: LlmServiceClient,
    private val messageJpaRepository: MessageJpaRepository,
    private val conversationJpaRepository: ConversationJpaRepository,
    private val objectMapper: ObjectMapper
) {
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    open fun handleMessageSentEvent(event: MessageSentEvent) {
        try {
            val chatRequest = ChatRequest(
                userId = event.userId,
                message = event.content
            )
            val llmResponse = llmServiceClient.sendChatMessage(chatRequest)
            if (llmResponse.success && llmResponse.data != null) {
                val chatResponse = objectMapper.convertValue(llmResponse.data, ChatResponse::class.java)
                val conversation = conversationJpaRepository.findById(event.conversationId)
                    .orElseThrow { IllegalStateException("대화를 찾을 수 없습니다: ${event.conversationId}") }
                val aiMessage = Message().apply {
                    this.conversationId = conversation
                    this.content = chatResponse.response
                    this.role = ConversationParticipantType.ASSISTANT
                    this.timestamp = LocalDateTime.now()
                }
                val savedAiMessage = messageJpaRepository.save(aiMessage)
                logger().info(
                    "AI 응답 메시지 저장 완료 - messageId: ${savedAiMessage.id}, " +
                            "conversationId: ${event.conversationId}"
                )
            } else {
                logger().error(
                    "LLM 서버 응답 실패 - error: ${llmResponse.error?.message}, " +
                            "conversationId: ${event.conversationId}"
                )
            }
        } catch (e: Exception) {
            logger().error(
                "메시지 처리 중 오류 발생 - conversationId: ${event.conversationId}, " +
                        "messageId: ${event.messageId}",
                e
            )
        }
    }
}