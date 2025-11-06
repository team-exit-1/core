package team.exit_1.repo.backend.core.service.domain.message.data.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

data class ContextUsageInfo(
    @field:Schema(description = "사용된 이전 대화 수", example = "5")
    @field:JsonProperty("total_conversations")
    val totalConversations: Int,
    @field:Schema(description = "가장 높은 유사도 점수", example = "0.95")
    @field:JsonProperty("top_score")
    val topScore: Float,
)
