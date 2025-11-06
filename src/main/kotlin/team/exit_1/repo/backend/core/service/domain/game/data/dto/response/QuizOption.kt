package team.exit_1.repo.backend.core.service.domain.game.data.dto.response

import com.fasterxml.jackson.annotation.JsonProperty

data class QuizOption(
    @field:JsonProperty("id")
    val id: String,
    @field:JsonProperty("text")
    val text: String,
)
