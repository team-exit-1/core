package team.exit_1.repo.backend.core.service.domain.game.data.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

data class SubmitQuizAnswerRequest(
    @field:Schema(description = "퀴즈 ID", example = "1")
    @field:JsonProperty("id")
    val quizId: Long,
    @field:Schema(description = "사용자 답변", example = "서울")
    @field:JsonProperty("correctAnswer")
    val answer: String,
)
