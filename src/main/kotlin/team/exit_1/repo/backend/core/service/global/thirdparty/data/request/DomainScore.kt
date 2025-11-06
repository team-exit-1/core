package team.exit_1.repo.backend.core.service.global.thirdparty.data.request

import com.fasterxml.jackson.annotation.JsonProperty

data class DomainScore(
    @field:JsonProperty("domain")
    val domain: String,
    @field:JsonProperty("score")
    val score: Int,
    @field:JsonProperty("insights")
    val insights: List<String>,
    @field:JsonProperty("analysis")
    val analysis: String,
)
