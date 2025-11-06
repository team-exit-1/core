package team.exit_1.repo.backend.core.service.domain.profile.data.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

data class PersonalInfoResponse(
    @field:Schema(description = "개인정보 ID", example = "550e8400-e29b-41d4-a716-446655440000")
    @field:JsonProperty("id")
    val id: String,
    @field:Schema(description = "사용자 ID", example = "user_2419")
    @field:JsonProperty("user_id")
    val userId: String,
    @field:Schema(description = "내용", example = "혈액형은 A형입니다")
    @field:JsonProperty("content")
    val content: String,
    @field:Schema(description = "카테고리", example = "medical")
    @field:JsonProperty("category")
    val category: String,
    @field:Schema(description = "중요도", example = "high")
    @field:JsonProperty("importance")
    val importance: String,
    @field:Schema(description = "생성일시", example = "2024-01-01T00:00:00Z")
    @field:JsonProperty("created_at")
    val createdAt: String,
    @field:Schema(description = "수정일시", example = "2024-01-01T00:00:00Z")
    @field:JsonProperty("updated_at")
    val updatedAt: String,
)
