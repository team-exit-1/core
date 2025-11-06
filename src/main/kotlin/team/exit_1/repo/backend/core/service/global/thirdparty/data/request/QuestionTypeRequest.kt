package team.exit_1.repo.backend.core.service.global.thirdparty.data.request

import com.fasterxml.jackson.annotation.JsonValue
import team.exit_1.repo.backend.core.service.domain.game.data.constant.QuestionType

enum class QuestionTypeRequest(
    val jsonValue: String,
) {
    FILL_IN_BLANK("fill_in_blank"),
    MULTIPLE_CHOICE("multiple_choice"),
    ;

    @JsonValue
    fun toJson(): String = jsonValue

    companion object {
        fun from(type: QuestionType): QuestionTypeRequest =
            when (type) {
                QuestionType.FILL_IN_BLANK -> FILL_IN_BLANK
                QuestionType.MULTIPLE_CHOICE -> MULTIPLE_CHOICE
            }
    }
}
