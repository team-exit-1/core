package team.exit_1.repo.backend.core.service.domain.data.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

data class SubmitQuizAnswerRequest(
    @field:Schema(description = "퀴즈 ID", example = "1")
    @field:JsonProperty("quiz_id")
    val quizId: Long,

    @field:Schema(description = "사용자 답변", example = "서울")
    @field:JsonProperty("answer")
    val answer: String
)
