package team.exit_1.repo.backend.core.service.domain.game.data.dto.response

import com.fasterxml.jackson.annotation.JsonValue
import team.exit_1.repo.backend.core.service.domain.game.data.constant.QuestionType

enum class QuestionTypeDto(
    val jsonValue: String,
) {
    FILL_IN_BLANK("fill_in_blank"),
    MULTIPLE_CHOICE("multiple_choice"),
    ;

    @JsonValue
    fun toJson(): String = jsonValue

    companion object {
        fun from(type: QuestionType): QuestionTypeDto =
            when (type) {
                QuestionType.FILL_IN_BLANK -> FILL_IN_BLANK
                QuestionType.MULTIPLE_CHOICE -> MULTIPLE_CHOICE
            }
    }
}
