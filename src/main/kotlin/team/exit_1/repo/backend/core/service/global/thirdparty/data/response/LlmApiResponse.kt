package team.exit_1.repo.backend.core.service.global.thirdparty.data.response

data class LlmApiResponse(
    val success: Boolean,
    val data: Map<String, Any>?,
    val error: LlmErrorInfo?,
    val metadata: LlmMetadata?
)