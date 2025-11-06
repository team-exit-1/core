package team.exit_1.repo.backend.core.service.domain.profile.data.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

data class CreatePersonalInfoRequest(
    @field:Schema(description = "내용", example = "혈액형은 A형입니다")
    @field:JsonProperty("content")
    val content: String,
    @field:Schema(description = "카테고리", example = "medical")
    @field:JsonProperty("category")
    val category: String,
    @field:Schema(description = "중요도", example = "high", allowableValues = ["high", "medium", "low"])
    @field:JsonProperty("importance")
    val importance: String,
)
