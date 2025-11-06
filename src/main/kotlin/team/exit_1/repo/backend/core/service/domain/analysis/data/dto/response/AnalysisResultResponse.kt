package team.exit_1.repo.backend.core.service.domain.analysis.data.dto.response

data class AnalysisResultResponse(
    val userId: String,
    val domains: List<DomainAnalysisDto>,
    val report: String,
    val analyzedAt: String,
)
