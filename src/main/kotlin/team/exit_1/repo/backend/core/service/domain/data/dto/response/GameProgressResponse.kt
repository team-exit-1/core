package team.exit_1.repo.backend.core.service.domain.data.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

data class GameProgressResponse(
    @field:Schema(description = "세션 ID", example = "session_550e8400-e29b-41d4-a716-446655440000")
    @field:JsonProperty("session_id")
    val sessionId: String,

    @field:Schema(description = "총 점수", example = "100")
    @field:JsonProperty("total_score")
    val totalScore: Int,

    @field:Schema(description = "시도한 퀴즈 수", example = "10")
    @field:JsonProperty("total_attempts")
    val totalAttempts: Long,

    @field:Schema(description = "정답 수", example = "8")
    @field:JsonProperty("correct_answers")
    val correctAnswers: Long,

    @field:Schema(description = "정답률", example = "80.0")
    @field:JsonProperty("accuracy_rate")
    val accuracyRate: Double
)
