package team.exit_1.repo.backend.core.service.domain.analysis.service

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.ApplicationEventPublisher
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import team.exit_1.repo.backend.core.service.domain.analysis.data.dto.response.AnalysisResultResponse
import team.exit_1.repo.backend.core.service.domain.analysis.data.dto.response.DomainAnalysisDto
import team.exit_1.repo.backend.core.service.domain.analysis.data.repository.AnalysisJpaRepository
import team.exit_1.repo.backend.core.service.domain.analysis.event.AnalysisGeneratedEvent
import team.exit_1.repo.backend.core.service.global.common.error.exception.ExpectedException
import team.exit_1.repo.backend.core.service.global.config.logger
import team.exit_1.repo.backend.core.service.global.thirdparty.client.LlmServiceClient
import team.exit_1.repo.backend.core.service.global.thirdparty.data.request.AnalysisRequest
import team.exit_1.repo.backend.core.service.global.thirdparty.data.request.DomainScore
import team.exit_1.repo.backend.core.service.global.thirdparty.data.request.ReportGenerationRequest
import team.exit_1.repo.backend.core.service.global.thirdparty.data.response.DomainAnalysisOnlyResponse
import team.exit_1.repo.backend.core.service.global.thirdparty.data.response.ReportOnlyResponse
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class GenerateAnalysisService(
    private val llmServiceClient: LlmServiceClient,
    private val objectMapper: ObjectMapper,
    private val eventPublisher: ApplicationEventPublisher,
    private val analysisJpaRepository: AnalysisJpaRepository,
) {
    companion object {
        private const val CACHE_DURATION_HOURS = 24L
    }

    fun execute(userId: String): AnalysisResultResponse {
        val oneDayAgo = LocalDateTime.now().minusHours(CACHE_DURATION_HOURS)
        val recentAnalysis = analysisJpaRepository.findFirstByUserIdOrderByCreatedAtDesc(userId)

        if (recentAnalysis != null && recentAnalysis.createdAt!! >= oneDayAgo) {
            logger().info("최근 분석 결과 발견 (${recentAnalysis.createdAt}) - 캐시된 결과 반환, userId: $userId")

            val domains =
                objectMapper.readValue(
                    recentAnalysis.domains!!,
                    object : TypeReference<List<DomainAnalysisDto>>() {},
                )

            return AnalysisResultResponse(
                userId = recentAnalysis.userId!!,
                domains = domains,
                report = recentAnalysis.report!!,
                analyzedAt = recentAnalysis.analyzedAt!!.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            )
        }

        logger().info("최근 분석 결과 없음 - 새로운 분석 생성, userId: $userId")

        val domainResponse = llmServiceClient.generateDomainAnalysis(AnalysisRequest(userId = userId))

        if (!domainResponse.success || domainResponse.data == null) {
            throw ExpectedException(
                message = "LLM 서버에서 도메인 분석에 실패했습니다: ${domainResponse.error?.message}",
                statusCode = HttpStatus.INTERNAL_SERVER_ERROR,
            )
        }

        val domainAnalysisResponse = objectMapper.convertValue(domainResponse.data, DomainAnalysisOnlyResponse::class.java)

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

        val analysisResult =
            AnalysisResultResponse(
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

        eventPublisher.publishEvent(
            AnalysisGeneratedEvent(
                userId = userId,
                analysisResult = analysisResult,
            ),
        )
        return analysisResult
    }
}
