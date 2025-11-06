package team.exit_1.repo.backend.core.service.global.thirdparty.data.response

import com.fasterxml.jackson.annotation.JsonProperty

data class ContextUsage(
    @field:JsonProperty("total_conversations")
    val totalConversations: Int,

    @field:JsonProperty("top_score")
    val topScore: Float
)

