package team.exit_1.repo.backend.core.service.global.thirdparty.data.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class LlmMetadata(
    val timestamp: String = "",
    @field:JsonProperty("request_id")
    val requestId: String = "",
)
