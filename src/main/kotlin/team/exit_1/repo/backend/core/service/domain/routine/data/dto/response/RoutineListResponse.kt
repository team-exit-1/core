package team.exit_1.repo.backend.core.service.domain.routine.data.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

data class RoutineListResponse(
    @field:Schema(description = "루틴 목록")
    @field:JsonProperty("items")
    val items: List<RoutineResponse>,

    @field:Schema(description = "총 개수", example = "5")
    @field:JsonProperty("total")
    val total: Int
)