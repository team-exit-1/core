package team.exit_1.repo.backend.core.service.domain.game.data.constant

enum class QuestionType(val jsonValue: String) {
    FILL_IN_BLANK("fill_in_blank"),
    MULTIPLE_CHOICE("multiple_choice");

    val lowercase: String = jsonValue

    companion object {
        fun fromString(value: String): QuestionType? {
            return entries.find {
                it.name.equals(value, ignoreCase = true) ||
                it.jsonValue.equals(value, ignoreCase = true)
            }
        }
    }
}