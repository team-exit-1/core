package team.exit_1.repo.backend.core.service.global.thirdparty.data.response

import com.fasterxml.jackson.annotation.JsonProperty

data class AnalysisResponse(
    @field:JsonProperty("user_id")
    val userId: String,
    val domains: List<DomainAnalysis>,
    val report: String,
    @field:JsonProperty("analyzed_at")
    val analyzedAt: String,
)

data class DomainAnalysis(
    val domain: String,
    val score: Int,
    val insights: List<String>,
    val analysis: String,
)
