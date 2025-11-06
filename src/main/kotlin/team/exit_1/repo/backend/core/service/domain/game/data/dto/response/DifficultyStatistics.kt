package team.exit_1.repo.backend.core.service.domain.game.data.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

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
    val accuracyRate: Double,
)
