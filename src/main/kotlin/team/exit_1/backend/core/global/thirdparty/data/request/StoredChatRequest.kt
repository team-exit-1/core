package team.exit_1.backend.core.global.thirdparty.data.request

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class StoredChatRequest(
    @param:JsonProperty("user_id")
    val userId: String,
    @param:JsonProperty("conversation_id")
    val conversationId: String,
    @param:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    val timestamp: LocalDateTime,
    // val messages: List<Message>, TODO: implement Message class
)