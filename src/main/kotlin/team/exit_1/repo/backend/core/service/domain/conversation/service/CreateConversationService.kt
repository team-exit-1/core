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
import java.util.UUID

@Service
class CreateConversationService(
    private val conversationJpaRepository: ConversationJpaRepository,
    private val messageJpaRepository: MessageJpaRepository,
    private val llmServiceClient: LlmServiceClient,
    private val objectMapper: ObjectMapper,
) {
    private final val greetingMessage =
        "오늘 무슨 일이 있었는지 나에게 물어봐줘. ( 이 질문에 대해선 답변을 하지말고 너가 먼저 말을 거는 것 처럼 해줘 )" +
            "당신은 치매 예방 및 완화를 돕는 대화형 AI입니다.  \n" +
            "사용자는 기억력 저하나 인지력 감퇴를 겪고 있을 수 있으며, 당신의 목표는 **따뜻하고 친근한 음성 대화를 통해 사용자의 두뇌 활동을 자극하고 정서적 안정감을 주는 것**입니다.  \n" +
            "\n" +
            "지금은 사용자가 전화를 받았을 때의 상황입니다.  \n" +
            "당신은 통화가 연결되면 **먼저 인사를 건네고**, **이전 대화의 내용을 짧게 상기시킨 뒤**, **오늘의 대화를 자연스럽게 이어가야 합니다.**  \n" +
            "\n" +
            "다음 원칙을 따르세요:\n" +
            "\n" +
            "1. 목소리 톤은 **따뜻하고 느긋하며**, **친근한 말투**를 사용하세요.  \n" +
            "3. 이전 대화를 언급할 때는 **핵심 주제나 감정적인 요소**만 간단히 요약하세요.  \n" +
            "   - 예: “어제는 강아지 산책 이야기했었죠.”  \n" +
            "   - “지난번엔 좋아하는 음식 이야기했었어요.”  \n" +
            "4. 바로 이어질 대화는 **인지 자극형 질문**이나 **일상 회상형 질문**으로 자연스럽게 유도하세요.  \n" +
            "   - 예: “오늘은 산책 다녀오셨어요?”  \n" +
            "   - “요즘 날씨가 쌀쌀해졌는데, 따뜻한 차는 자주 드시나요?”  \n" +
            "   - 추석인데, 가족분들 오시나요? 오시면 누구 오시나요?\n" +
            "5. 절대 사용자를 검사하거나 지적하지 말고, 항상 **긍정적 피드백**과 **공감의 말**을 포함하세요.  \n" +
            "6. 이전 대화 기록은 아래 요약 정보를 참고하여 자연스럽게 연결하세요.  \n" +
            "7. 문장은 1문장정도로 짧게 상호작용하면서 대화를 이어가세요.\n" +
            "\n" +
            "[이전 대화 요약]  \n" +
            "{{previous_summary}}"

    @Transactional
    fun execute(): ConversationResponse {
        val conversationId = "conv_${UUID.randomUUID()}"

        val conversation =
            conversationJpaRepository.save(
                Conversation().apply {
                    this.userId = MOCK_USER_ID
                    this.id = conversationId
                    this.timestamp = LocalDateTime.now()
                },
            )

        val greetingRequest =
            ChatRequest(
                userId = MOCK_USER_ID,
                message = greetingMessage,
            )

        logger().info("대화 시작 인사말 요청 - conversationId: $conversationId, userId: $MOCK_USER_ID")

        val llmResponse = llmServiceClient.sendChatMessage(greetingRequest)

        if (!llmResponse.success || llmResponse.data == null) {
            throw ExpectedException(
                message = "LLM 서버 인사말 응답 실패: ${llmResponse.error?.message ?: "알 수 없는 오류"}",
                statusCode = HttpStatus.INTERNAL_SERVER_ERROR,
            )
        }

        val chatResponse = objectMapper.convertValue(llmResponse.data, ChatResponse::class.java)
        val greetingMessage = chatResponse.response

        val aiGreetingMessage =
            Message().apply {
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
            initialGreeting = greetingMessage,
        )
    }
}
