package team.exit_1.repo.backend.core.service.domain.analysis.data.dto.response

data class DomainAnalysisDto(
    val domain: String,
    val score: Int,
    val insights: List<String>,
    val analysis: String,
)
