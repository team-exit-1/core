package team.exit_1.repo.backend.core.service.global.thirdparty.data.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class LlmApiResponse(
    val success: Boolean = false,
    val data: Map<String, Any>? = null,
    val error: LlmErrorInfo? = null,
    val metadata: LlmMetadata? = null,
)
