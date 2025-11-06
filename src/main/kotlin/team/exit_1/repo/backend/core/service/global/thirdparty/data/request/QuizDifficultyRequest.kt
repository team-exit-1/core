package team.exit_1.repo.backend.core.service.global.thirdparty.data.request

import com.fasterxml.jackson.annotation.JsonValue
import team.exit_1.repo.backend.core.service.domain.game.data.constant.QuizDifficulty

enum class QuizDifficultyRequest {
    EASY,
    MEDIUM,
    HARD,
    ;

    @JsonValue
    fun toJson(): String = this.name.lowercase()

    companion object {
        fun from(difficulty: QuizDifficulty): QuizDifficultyRequest =
            when (difficulty) {
                QuizDifficulty.EASY -> EASY
                QuizDifficulty.MEDIUM -> MEDIUM
                QuizDifficulty.HARD -> HARD
            }
    }
}
