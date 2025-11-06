package team.exit_1.repo.backend.core.service.global.thirdparty.data.response

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class ChatResponse(
    @JsonProperty("conversation_id")
    val conversationId: String?,

    @JsonProperty("message")
    val message: String?,

    @JsonProperty("response")
    val response: String,

    @JsonProperty("context_used")
    val contextUsed: ContextUsage?,

    @JsonProperty("created_at")
    val createdAt: LocalDateTime?
)

