package team.exit_1.repo.backend.core.service.global.thirdparty.data.response

data class StoreConversationError(
    val code: String,
    val message: String,
    val details: Map<String, Any>?,
)
