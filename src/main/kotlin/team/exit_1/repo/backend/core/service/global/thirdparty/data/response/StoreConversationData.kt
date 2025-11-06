package team.exit_1.repo.backend.core.service.global.thirdparty.data.response

import com.fasterxml.jackson.annotation.JsonProperty

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