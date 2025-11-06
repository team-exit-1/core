package team.exit_1.repo.backend.core.service.domain.routine.data.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class RoutineResponse(
    @field:Schema(description = "루틴 ID", example = "1")
    @field:JsonProperty("id")
    val id: Long,

    @field:Schema(description = "사용자 ID", example = "user_2419")
    @field:JsonProperty("user_id")
    val userId: String,

    @field:Schema(description = "루틴 제목", example = "운동하기")
    @field:JsonProperty("title")
    val title: String,

    @field:Schema(description = "루틴 내용", example = "아침 조깅 30분")
    @field:JsonProperty("content")
    val content: String,

    @field:Schema(description = "알림 시각", example = "06:00")
    @field:JsonProperty("times")
    val times: String,

    @field:Schema(description = "생성일시", example = "2024-01-01T00:00:00")
    @field:JsonProperty("created_at")
    val createdAt: LocalDateTime,

    @field:Schema(description = "수정일시", example = "2024-01-01T00:00:00")
    @field:JsonProperty("updated_at")
    val updatedAt: LocalDateTime
)