package team.exit_1.repo.backend.core.service.global.thirdparty.data.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class LlmErrorInfo(
    val code: String = "",
    val message: String = "",
    val details: Map<String, Any>? = null,
)
