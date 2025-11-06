package team.exit_1.repo.backend.core.service.global.thirdparty.data.response

data class LlmErrorInfo(
    val code: String,
    val message: String,
    val details: Map<String, Any>?
)