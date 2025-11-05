package team.exit_1.repo.backend.core.service.domain.game.data.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import team.exit_1.repo.backend.core.service.domain.game.data.constant.QuizDifficulty

data class QuizResponse(
    @field:Schema(description = "퀴즈 ID", example = "1")
    @field:JsonProperty("quiz_id")
    val quizId: Long,

    @field:Schema(description = "질문", example = "당신의 고향은 어디인가요?")
    @field:JsonProperty("question")
    val question: String,

    @field:Schema(description = "난이도", example = "EASY")
    @field:JsonProperty("difficulty")
    val difficulty: QuizDifficulty,

    @field:Schema(description = "카테고리", example = "자전적 기억")
    @field:JsonProperty("category")
    val category: String?,

    @field:Schema(description = "힌트", example = "태어난 곳을 떠올려보세요")
    @field:JsonProperty("hint")
    val hint: String?
)
