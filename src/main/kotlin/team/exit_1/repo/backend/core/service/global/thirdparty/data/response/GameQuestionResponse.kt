package team.exit_1.repo.backend.core.service.global.thirdparty.data.response

import com.fasterxml.jackson.annotation.JsonProperty
import team.exit_1.repo.backend.core.service.domain.game.data.constant.QuestionType
import team.exit_1.repo.backend.core.service.domain.game.data.constant.QuizDifficulty

data class GameQuestionResponse(
    @field:JsonProperty("question_id")
    val questionId: String,
    @field:JsonProperty("question_type")
    val questionType: String, // lowercase string from LLM (fill_in_blank or multiple_choice)
    val question: String,
    val options: List<QuestionOption>? = null, // fill_in_blank and multiple_choice
    @field:JsonProperty("correct_answer")
    val correctAnswer: String,
    @field:JsonProperty("based_on_conversation")
    val basedOnConversation: String,
    val difficulty: String, // lowercase string from LLM
    val metadata: QuestionMetadata,
) {
    fun parseQuestionType(): QuestionType? = QuestionType.fromString(questionType)

    fun parseDifficulty(): QuizDifficulty? = QuizDifficulty.fromString(difficulty)
}
