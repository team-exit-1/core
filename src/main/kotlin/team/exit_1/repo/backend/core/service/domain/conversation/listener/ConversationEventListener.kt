package team.exit_1.repo.backend.core.service.domain.conversation.listener

import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import team.exit_1.repo.backend.core.service.domain.conversation.event.ConversationDisabledEvent
import team.exit_1.repo.backend.core.service.domain.message.data.repository.MessageJpaRepository
import team.exit_1.repo.backend.core.service.global.config.logger
import team.exit_1.repo.backend.core.service.global.thirdparty.client.RagServiceClient
import team.exit_1.repo.backend.core.service.global.thirdparty.data.dto.MessageDto
import team.exit_1.repo.backend.core.service.global.thirdparty.data.request.StoreConversationRequest

@Component
class ConversationEventListener(
    private val messageJpaRepository: MessageJpaRepository,
    private val ragServiceClient: RagServiceClient,
) {
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handleConversationDisabled(event: ConversationDisabledEvent) {
        val conversationId = event.conversationId
        val conversation = event.conversation

        try {
            val messages = messageJpaRepository.findAllByConversationIdOrderByTimestampAsc(conversation)
            if (messages.isEmpty()) {
                logger().info("대화 $conversationId 에 메시지가 없어 RAG 저장을 건너뜁니다.")
                return
            }

            val messageDtos =
                messages.map { message ->
                    MessageDto(
                        role = message.role?.lowercase ?: "user",
                        content = message.content,
                    )
                }

            val storeRequest =
                StoreConversationRequest(
                    conversationId = conversationId,
                    messages = messageDtos,
                    metadata =
                        mapOf(
                            "source" to "core_service",
                            "user_id" to conversation.userId!!,
                        ),
                )

            val response = ragServiceClient.storeConversation(storeRequest)
            if (response.success) {
                logger().info("성공적으로 대화 $conversationId 를 RAG 서비스에 저장했습니다.")
            } else {
                logger().error("RAG 서비스에 대화 $conversationId 저장 실패: ${response.error?.message}")
            }
        } catch (e: Exception) {
            logger().error("RAG 서비스에 대화 $conversationId 저장 중 오류 발생", e)
        }
    }
}
