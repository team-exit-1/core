package team.exit_1.repo.backend.core.service.domain.game.data.dto.response

import com.fasterxml.jackson.annotation.JsonValue
import team.exit_1.repo.backend.core.service.domain.game.data.constant.QuestionType

enum class QuestionTypeDto {
    OX,
    MULTIPLE_CHOICE;

    @JsonValue
    fun toJson(): String = this.name.lowercase()

    companion object {
        fun from(type: QuestionType): QuestionTypeDto {
            return when (type) {
                QuestionType.OX -> OX
                QuestionType.MULTIPLE_CHOICE -> MULTIPLE_CHOICE
            }
        }
    }
}