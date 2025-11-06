package team.exit_1.repo.backend.core.service.global.thirdparty.data.response

data class StoreConversationResponse(
    val success: Boolean,
    val data: StoreConversationData?,
    val error: StoreConversationError?,
    val metadata: ResponseMetadata?
)