package team.exit_1.repo.backend.core.service.global.thirdparty.data.response

import com.fasterxml.jackson.annotation.JsonProperty

data class NextQuestionSuggestion(
    val difficulty: String,
    @field:JsonProperty("topic_preference")
    val topicPreference: String,
)
