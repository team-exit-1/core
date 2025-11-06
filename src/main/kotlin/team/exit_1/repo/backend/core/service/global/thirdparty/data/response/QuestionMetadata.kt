package team.exit_1.repo.backend.core.service.global.thirdparty.data.response

import com.fasterxml.jackson.annotation.JsonProperty

data class QuestionMetadata(
    val topic: String,
    @field:JsonProperty("memory_score")
    val memoryScore: Float,
    @field:JsonProperty("days_since_conversation")
    val daysSinceConversation: Int,
)
