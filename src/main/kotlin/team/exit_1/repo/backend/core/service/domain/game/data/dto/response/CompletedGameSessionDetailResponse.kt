package team.exit_1.repo.backend.core.service.domain.game.data.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import team.exit_1.repo.backend.core.service.domain.game.data.constant.GameSessionStatus
import team.exit_1.repo.backend.core.service.domain.game.data.constant.QuizDifficulty
import java.time.LocalDateTime

@Schema(description = "완료된 게임 세션 상세 정보 응답")
data class CompletedGameSessionDetailResponse(
    @field:Schema(description = "세션 ID", example = "session_550e8400-e29b-41d4-a716-446655440000")
    @field:JsonProperty("session_id")
    val sessionId: String,
    @field:Schema(description = "사용자 ID", example = "user_123")
    @field:JsonProperty("user_id")
    val userId: String,
    @field:Schema(description = "세션 상태", example = "COMPLETED")
    @field:JsonProperty("status")
    val status: GameSessionStatus,
    @field:Schema(description = "시작 시간", example = "2024-01-01T10:00:00")
    @field:JsonProperty("start_time")
    val startTime: LocalDateTime,
    @field:Schema(description = "종료 시간", example = "2024-01-01T11:30:00")
    @field:JsonProperty("end_time")
    val endTime: LocalDateTime?,
    @field:Schema(description = "최종 난이도", example = "MEDIUM")
    @field:JsonProperty("final_difficulty")
    val finalDifficulty: QuizDifficulty,
    @field:Schema(description = "게임 통계 정보")
    @field:JsonProperty("statistics")
    val statistics: GameStatistics,
    @field:Schema(description = "출제된 문제와 답변 목록")
    @field:JsonProperty("quiz_attempts")
    val quizAttempts: List<QuizAttemptDetail>,
)
