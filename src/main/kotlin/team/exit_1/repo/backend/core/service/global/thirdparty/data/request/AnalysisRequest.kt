package team.exit_1.repo.backend.core.service.global.thirdparty.data.request

import com.fasterxml.jackson.annotation.JsonProperty

data class AnalysisRequest(
    @field:JsonProperty("user_id")
    val userId: String,
)
