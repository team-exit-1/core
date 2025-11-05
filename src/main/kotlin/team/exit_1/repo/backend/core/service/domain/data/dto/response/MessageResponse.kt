package team.exit_1.repo.backend.core.service.domain.data.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import team.exit_1.repo.backend.core.service.domain.data.constant.ConversationParticipantType
import java.time.LocalDateTime

data class MessageResponse(
    @field:Schema(description = "메시지 ID", example = "1")
    @field:JsonProperty("message_id")
    val messageId: Long,

    @field:Schema(description = "메시지 내용", example = "안녕하세요")
    @field:JsonProperty("content")
    val content: String,

    @field:Schema(description = "메시지 송신자", example = "USER")
    @field:JsonProperty("role")
    val role: ConversationParticipantType,

    @field:Schema(description = "메시지 생성 시간", example = "2024-01-01T00:00:00")
    @field:JsonProperty("timestamp")
    val timestamp: LocalDateTime
)
