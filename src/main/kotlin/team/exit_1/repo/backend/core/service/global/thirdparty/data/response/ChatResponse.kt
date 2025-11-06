package team.exit_1.repo.backend.core.service.global.thirdparty.data.response

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class ChatResponse(
    @field:JsonProperty("conversation_id")
    val conversationId: String?,

    @field:JsonProperty("message")
    val message: String?,

    @field:JsonProperty("response")
    val response: String,

    @field:JsonProperty("context_used")
    val contextUsed: ContextUsage?,

    @field:JsonProperty("created_at")
    val createdAt: LocalDateTime?
)

