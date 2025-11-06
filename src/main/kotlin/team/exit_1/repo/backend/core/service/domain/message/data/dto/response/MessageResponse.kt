package team.exit_1.repo.backend.core.service.domain.message.data.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class MessageResponse(
    @field:Schema(description = "AI 응답 메시지 ID", example = "2")
    @field:JsonProperty("message_id")
    val messageId: Long,
    @field:Schema(description = "AI 응답 내용", example = "안녕하세요! 무엇을 도와드릴까요?")
    @field:JsonProperty("content")
    val content: String,
    @field:Schema(description = "AI 응답 생성 시간", example = "2024-01-01T00:00:01")
    @field:JsonProperty("timestamp")
    val timestamp: LocalDateTime,
    @field:Schema(description = "대화 컨텍스트 사용 정보")
    @field:JsonProperty("context_used")
    val contextUsed: ContextUsageInfo?,
)
