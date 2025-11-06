package team.exit_1.repo.backend.core.service.global.thirdparty.data.response

import com.fasterxml.jackson.annotation.JsonProperty

data class DomainAnalysisOnlyResponse(
    @field:JsonProperty("user_id")
    val userId: String,
    val domains: List<DomainAnalysis>,
    @field:JsonProperty("analyzed_at")
    val analyzedAt: String,
)
