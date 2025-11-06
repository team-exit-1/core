package team.exit_1.repo.backend.core.service.global.thirdparty.data.response

import com.fasterxml.jackson.annotation.JsonProperty

data class ContextUsage(
    @JsonProperty("total_conversations")
    val totalConversations: Int,

    @JsonProperty("top_score")
    val topScore: Float
)

