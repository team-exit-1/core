package team.exit_1.repo.backend.core.service.global.thirdparty.data.request

import com.fasterxml.jackson.annotation.JsonProperty

data class PersonalInfoCreateRequest(
    @field:JsonProperty("user_id")
    val userId: String,

    @field:JsonProperty("content")
    val content: String,

    @field:JsonProperty("category")
    val category: String,

    @field:JsonProperty("importance")
    val importance: String
)