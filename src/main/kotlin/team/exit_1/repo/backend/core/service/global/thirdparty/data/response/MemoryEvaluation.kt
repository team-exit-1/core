package team.exit_1.repo.backend.core.service.global.thirdparty.data.response

import com.fasterxml.jackson.annotation.JsonProperty

data class MemoryEvaluation(
    val topic: String,
    @field:JsonProperty("retention_score")
    val retentionScore: Float,
    val confidence: String, // "high", "medium", "low"
    val recommendation: String,
)
