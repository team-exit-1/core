package team.exit_1.repo.backend.core.service.domain.conversation.data.dto.response

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import team.exit_1.repo.backend.core.service.domain.conversation.data.constant.ConversationStatus
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ConversationResponse(
    @field:JsonProperty("conversation_id")
    @field:Schema(description = "대화 ID", example = "conv_001", name = "conversation_id")
    val conversationId: String,
    @field:JsonProperty("user_id")
    @field:Schema(description = "사용자 ID", example = "user_2419", name = "user_id")
    val userId: String,
    @field:JsonProperty("conversation_status")
    @field:Schema(description = "대화 상태", example = "ACTIVE", name = "conversation_status")
    val conversationStatus: ConversationStatus,
    @field:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @field:Schema(description = "대화 생성시각 타임스탬프", example = "2024-01-01T12:00:00Z")
    val timestamp: LocalDateTime,
    @field:JsonProperty("initial_greeting")
    @field:Schema(description = "AI의 첫 인사말", example = "안녕하세요! 저는 당신의 AI 친구입니다. 오늘 하루는 어떠셨나요?")
    val initialGreeting: String?,
)
