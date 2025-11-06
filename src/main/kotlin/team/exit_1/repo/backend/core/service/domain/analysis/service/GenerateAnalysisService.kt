package team.exit_1.repo.backend.core.service.domain.analysis.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import team.exit_1.repo.backend.core.service.domain.analysis.data.dto.response.AnalysisResultResponse
import team.exit_1.repo.backend.core.service.domain.analysis.data.dto.response.DomainAnalysisDto
import team.exit_1.repo.backend.core.service.global.common.error.exception.ExpectedException
import team.exit_1.repo.backend.core.service.global.thirdparty.client.LlmServiceClient
import team.exit_1.repo.backend.core.service.global.thirdparty.data.request.AnalysisRequest
import team.exit_1.repo.backend.core.service.global.thirdparty.data.request.DomainScore
import team.exit_1.repo.backend.core.service.global.thirdparty.data.request.ReportGenerationRequest
import team.exit_1.repo.backend.core.service.global.thirdparty.data.response.DomainAnalysisOnlyResponse
import team.exit_1.repo.backend.core.service.global.thirdparty.data.response.ReportOnlyResponse

@Service
class GenerateAnalysisService(
    private val llmServiceClient: LlmServiceClient,
    private val objectMapper: ObjectMapper,
) {
    fun execute(userId: String): AnalysisResultResponse {
        // Step 1: 도메인 분석 요청
        val domainResponse = llmServiceClient.generateDomainAnalysis(AnalysisRequest(userId = userId))

        if (!domainResponse.success || domainResponse.data == null) {
            throw ExpectedException(
                message = "LLM 서버에서 도메인 분석에 실패했습니다: ${domainResponse.error?.message}",
                statusCode = HttpStatus.INTERNAL_SERVER_ERROR,
            )
        }

        val domainAnalysisResponse = objectMapper.convertValue(domainResponse.data, DomainAnalysisOnlyResponse::class.java)

        // Step 2: 도메인 점수로 리포트 생성 요청
        val domainScores =
            domainAnalysisResponse.domains.map { domain ->
                DomainScore(
                    domain = domain.domain,
                    score = domain.score,
                    insights = domain.insights,
                    analysis = domain.analysis,
                )
            }

        val reportResponse = llmServiceClient.generateReport(ReportGenerationRequest(domains = domainScores))

        if (!reportResponse.success || reportResponse.data == null) {
            throw ExpectedException(
                message = "LLM 서버에서 리포트 생성에 실패했습니다: ${reportResponse.error?.message}",
                statusCode = HttpStatus.INTERNAL_SERVER_ERROR,
            )
        }

        val reportOnlyResponse = objectMapper.convertValue(reportResponse.data, ReportOnlyResponse::class.java)

        // 최종 결과 조합
        return AnalysisResultResponse(
            userId = domainAnalysisResponse.userId,
            domains =
                domainAnalysisResponse.domains.map { domain ->
                    DomainAnalysisDto(
                        domain = domain.domain,
                        score = domain.score,
                        insights = domain.insights,
                        analysis = domain.analysis,
                    )
                },
            report = reportOnlyResponse.report,
            analyzedAt = domainAnalysisResponse.analyzedAt,
        )
    }
}
