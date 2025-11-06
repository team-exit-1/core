package team.exit_1.repo.backend.core.service.domain.game.data.constant

enum class QuizDifficulty {
    EASY,
    MEDIUM,
    HARD,
    ;

    val lowercase: String = this.name.lowercase()

    companion object {
        fun fromString(value: String): QuizDifficulty? = entries.find { it.name.equals(value, ignoreCase = true) }
    }
}
