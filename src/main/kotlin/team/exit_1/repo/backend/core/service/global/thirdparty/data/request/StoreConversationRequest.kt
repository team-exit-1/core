package team.exit_1.repo.backend.core.service.global.thirdparty.data.request

import com.fasterxml.jackson.annotation.JsonProperty
import team.exit_1.repo.backend.core.service.global.thirdparty.data.dto.MessageDto

data class StoreConversationRequest(
    @field:JsonProperty("conversation_id")
    val conversationId: String,
    val messages: List<MessageDto>,
    val metadata: Map<String, String>? = null
)