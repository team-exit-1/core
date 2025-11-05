package team.exit_1.repo.backend.core.service.domain.conversation.service

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.exit_1.repo.backend.core.service.domain.conversation.data.constant.ConversationStatus
import team.exit_1.repo.backend.core.service.domain.conversation.data.dto.response.ConversationResponse
import team.exit_1.repo.backend.core.service.domain.conversation.data.repository.ConversationJpaRepository
import team.exit_1.repo.backend.core.service.domain.message.data.repository.MessageJpaRepository
import team.exit_1.repo.backend.core.service.global.common.error.exception.ExpectedException
import team.exit_1.repo.backend.core.service.global.thirdparty.client.RagServiceClient
import team.exit_1.repo.backend.core.service.global.thirdparty.data.dto.MessageDto
import team.exit_1.repo.backend.core.service.global.thirdparty.data.request.StoreConversationRequest

@Service
class DisableConversationService(
    private val conversationRepository: ConversationJpaRepository,
    private val messageJpaRepository: MessageJpaRepository,
    private val ragServiceClient: RagServiceClient
) {
    private val logger = LoggerFactory.getLogger(DisableConversationService::class.java)

    @Transactional
    fun execute(conversationId: String): ConversationResponse {
        val conversation = conversationRepository.findById(conversationId).orElseThrow {
            ExpectedException(message = "대화가 존재하지 않습니다.", HttpStatus.NOT_FOUND)
        }
        conversation.status = ConversationStatus.DISABLED
        val updatedConversation = conversationRepository.save(conversation)

        try {
            val messages = messageJpaRepository.findAllByConversationIdOrderByTimestampAsc(conversation)
            if (messages.isNotEmpty()) {
                val messageDtos = messages.map { message ->
                    MessageDto(
                        role = message.role?.lowercase ?: "user",
                        content = message.content
                    )
                }

                val storeRequest = StoreConversationRequest(
                    conversationId = conversationId,
                    messages = messageDtos,
                    metadata = mapOf(
                        "source" to "core_service",
                        "user_id" to conversation.userId!!
                    )
                )

                val response = ragServiceClient.storeConversation(storeRequest)
                if (response.success) {
                    logger.info("성공적으로 대화 $conversationId 를 RAG 서비스에 저장했습니다.")
                } else {
                    logger.error("RAG 서비스에 대화 $conversationId 저장 실패: ${response.error?.message}")
                }
            }
        } catch (e: Exception) {
            logger.error("Error while storing conversation $conversationId to RAG service", e)
        }

        return ConversationResponse(
            conversationId = updatedConversation.id!!,
            userId = updatedConversation.userId!!,
            conversationStatus = updatedConversation.status,
            timestamp = updatedConversation.timestamp!!
        )
    }
}