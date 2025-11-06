package team.exit_1.repo.backend.core.service.domain.routine.data.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

data class CreateRoutineRequest(
    @field:Schema(description = "루틴 제목", example = "운동하기")
    @field:JsonProperty("title")
    val title: String,

    @field:Schema(description = "루틴 내용", example = "아침 조깅 30분")
    @field:JsonProperty("content")
    val content: String,

    @field:Schema(description = "알림 시각", example = "06:00")
    @field:JsonProperty("times")
    val times: String
)