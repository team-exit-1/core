package team.exit_1.repo.backend.core.service.domain.game.data.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class QuizAttemptResponse(
    @field:Schema(description = "시도 ID", example = "1")
    @field:JsonProperty("attempt_id")
    val attemptId: Long,

    @field:Schema(description = "퀴즈 ID", example = "1")
    @field:JsonProperty("quiz_id")
    val quizId: Long,

    @field:Schema(description = "사용자 답변", example = "서울")
    @field:JsonProperty("user_answer")
    val userAnswer: String,

    @field:Schema(description = "정답 여부", example = "true")
    @field:JsonProperty("is_correct")
    val isCorrect: Boolean,

    @field:Schema(description = "획득 점수", example = "10")
    @field:JsonProperty("score")
    val score: Int,

    @field:Schema(description = "시도 시간", example = "2024-01-01T00:00:00")
    @field:JsonProperty("attempt_time")
    val attemptTime: LocalDateTime
)
