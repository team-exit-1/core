package team.exit_1.repo.backend.core.service.domain.game.data.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import team.exit_1.repo.backend.core.service.domain.game.data.constant.QuizDifficulty

@Schema(description = "게임 통계 정보")
data class GameStatistics(
    @field:Schema(description = "총 점수", example = "150")
    @field:JsonProperty("total_score")
    val totalScore: Int,

    @field:Schema(description = "총 문제 수", example = "10")
    @field:JsonProperty("total_quizzes")
    val totalQuizzes: Int,

    @field:Schema(description = "정답 수", example = "7")
    @field:JsonProperty("correct_answers")
    val correctAnswers: Int,

    @field:Schema(description = "오답 수", example = "3")
    @field:JsonProperty("wrong_answers")
    val wrongAnswers: Int,

    @field:Schema(description = "정답률 (%)", example = "70.0")
    @field:JsonProperty("accuracy_rate")
    val accuracyRate: Double,

    @field:Schema(description = "플레이 시간 (분)", example = "90")
    @field:JsonProperty("play_time_minutes")
    val playTimeMinutes: Long,

    @field:Schema(description = "난이도별 정답률")
    @field:JsonProperty("accuracy_by_difficulty")
    val accuracyByDifficulty: Map<QuizDifficulty, DifficultyStatistics>
)

@Schema(description = "난이도별 통계")
data class DifficultyStatistics(
    @field:Schema(description = "문제 수", example = "3")
    @field:JsonProperty("total")
    val total: Int,

    @field:Schema(description = "정답 수", example = "2")
    @field:JsonProperty("correct")
    val correct: Int,

    @field:Schema(description = "정답률 (%)", example = "66.67")
    @field:JsonProperty("accuracy_rate")
    val accuracyRate: Double
)
