package team.exit_1.repo.backend.core.service.global.thirdparty.data.request

import com.fasterxml.jackson.annotation.JsonProperty

data class GameResultRequest(
    @field:JsonProperty("user_id")
    val userId: String,
    @field:JsonProperty("question_id")
    val questionId: String,
    @field:JsonProperty("user_answer")
    val userAnswer: String,
    @field:JsonProperty("is_correct")
    val isCorrect: Boolean,
    @field:JsonProperty("response_time_ms")
    val responseTimeMs: Long? = null,
    @field:JsonProperty("game_session_id")
    val gameSessionId: String? = null
)