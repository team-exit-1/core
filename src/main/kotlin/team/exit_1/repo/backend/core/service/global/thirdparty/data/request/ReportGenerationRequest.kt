package team.exit_1.repo.backend.core.service.global.thirdparty.data.request

import com.fasterxml.jackson.annotation.JsonProperty

data class ReportGenerationRequest(
    @field:JsonProperty("domains")
    val domains: List<DomainScore>,
)
