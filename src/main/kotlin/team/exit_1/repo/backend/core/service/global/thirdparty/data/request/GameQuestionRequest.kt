package team.exit_1.repo.backend.core.service.global.thirdparty.data.request

import com.fasterxml.jackson.annotation.JsonProperty

data class GameQuestionRequest(
    @field:JsonProperty("user_id")
    val userId: String,
    @field:JsonProperty("question_type")
    val questionType: QuestionTypeRequest,
    @field:JsonProperty("difficulty_hint")
    val difficultyHint: QuizDifficultyRequest? = null,
)
