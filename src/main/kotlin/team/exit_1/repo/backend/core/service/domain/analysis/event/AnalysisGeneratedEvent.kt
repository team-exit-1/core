package team.exit_1.repo.backend.core.service.domain.analysis.event

import org.springframework.context.ApplicationEvent
import team.exit_1.repo.backend.core.service.domain.analysis.data.dto.response.AnalysisResultResponse

data class AnalysisGeneratedEvent(
    val userId: String,
    val analysisResult: AnalysisResultResponse,
) : ApplicationEvent(userId)