package team.exit_1.repo.backend.core.service.domain.message.data.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

data class SendMessageRequest(
    @field:Schema(description = "메시지 내용", example = "안녕하세요")
    @field:JsonProperty("content")
    val content: String,
)
