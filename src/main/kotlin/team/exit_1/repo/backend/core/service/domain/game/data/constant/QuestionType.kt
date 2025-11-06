package team.exit_1.repo.backend.core.service.domain.game.data.constant

enum class QuestionType {
    OX, MULTIPLE_CHOICE;

    val lowercase: String = this.name.lowercase()

    companion object {
        fun fromString(value: String): QuestionType? {
            return entries.find { it.name.equals(value, ignoreCase = true) }
        }
    }
}