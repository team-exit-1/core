package team.exit_1.repo.backend.core.service.domain.data.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import team.exit_1.repo.backend.core.service.domain.data.constant.GameSessionStatus
import team.exit_1.repo.backend.core.service.domain.data.constant.QuizDifficulty
import java.time.LocalDateTime

data class GameSessionResponse(
    @field:Schema(description = "세션 ID", example = "session_550e8400-e29b-41d4-a716-446655440000")
    @field:JsonProperty("session_id")
    val sessionId: String,

    @field:Schema(description = "대화 ID", example = "conv_550e8400-e29b-41d4-a716-446655440000")
    @field:JsonProperty("conversation_id")
    val conversationId: String,

    @field:Schema(description = "세션 상태", example = "IN_PROGRESS")
    @field:JsonProperty("status")
    val status: GameSessionStatus,

    @field:Schema(description = "시작 시간", example = "2024-01-01T00:00:00")
    @field:JsonProperty("start_time")
    val startTime: LocalDateTime,

    @field:Schema(description = "종료 시간", example = "2024-01-01T01:00:00")
    @field:JsonProperty("end_time")
    val endTime: LocalDateTime?,

    @field:Schema(description = "총 점수", example = "100")
    @field:JsonProperty("total_score")
    val totalScore: Int,

    @field:Schema(description = "현재 난이도", example = "EASY")
    @field:JsonProperty("current_difficulty")
    val currentDifficulty: QuizDifficulty
)
