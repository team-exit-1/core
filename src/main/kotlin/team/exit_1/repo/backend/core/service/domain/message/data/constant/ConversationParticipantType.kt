package team.exit_1.repo.backend.core.service.domain.message.data.constant

enum class ConversationParticipantType {
    USER,
    ASSISTANT,
    ;

    val lowercase: String = this.name.lowercase()
}
