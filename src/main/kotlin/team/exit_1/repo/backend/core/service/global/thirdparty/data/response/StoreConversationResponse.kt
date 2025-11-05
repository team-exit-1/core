package team.exit_1.repo.backend.core.service.global.thirdparty.data.response

import com.fasterxml.jackson.annotation.JsonProperty

data class StoreConversationResponse(
    val success: Boolean,
    val data: StoreConversationData?,
    val error: StoreConversationError?,
    val metadata: ResponseMetadata?
)

data class StoreConversationData(
    @field:JsonProperty("conversation_id")
    val conversationId: String,
    @field:JsonProperty("vectors_created")
    val vectorsCreated: Int,
    @field:JsonProperty("messages_stored")
    val messagesStored: Int,
    @field:JsonProperty("stored_at")
    val storedAt: String,
    @field:JsonProperty("processing_time_ms")
    val processingTimeMs: Int
)

data class StoreConversationError(
    val code: String,
    val message: String,
    val details: Map<String, Any>?
)

data class ResponseMetadata(
    val timestamp: String,
    @field:JsonProperty("request_id")
    val requestId: String
)