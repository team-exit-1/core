package team.exit_1.repo.backend.core.service.global.thirdparty.data.response

import com.fasterxml.jackson.annotation.JsonProperty

data class ReportOnlyResponse(
    @field:JsonProperty("report")
    val report: String,
)
