package team.exit_1.repo.backend.core.service.global.thirdparty.data.response

import com.fasterxml.jackson.annotation.JsonProperty

data class GameResultResponse(
    @field:JsonProperty("result_id")
    val resultId: String,
    @field:JsonProperty("memory_evaluation")
    val memoryEvaluation: MemoryEvaluation,
    @field:JsonProperty("next_question_suggestion")
    val nextQuestionSuggestion: NextQuestionSuggestion,
    @field:JsonProperty("stored_at")
    val storedAt: String,
)
