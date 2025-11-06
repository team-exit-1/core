package team.exit_1.repo.backend.core.service.domain.profile.data.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

data class PersonalInfoListResponse(
    @field:Schema(description = "개인정보 목록")
    @field:JsonProperty("items")
    val items: List<PersonalInfoResponse>,

    @field:Schema(description = "총 개수", example = "5")
    @field:JsonProperty("total")
    val total: Int,

    @field:Schema(description = "사용자 ID", example = "user_2419")
    @field:JsonProperty("user_id")
    val userId: String
)