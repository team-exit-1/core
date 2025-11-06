package team.exit_1.repo.backend.core.service.domain.analysis.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import team.exit_1.repo.backend.core.service.domain.analysis.data.dto.response.AnalysisResultResponse
import team.exit_1.repo.backend.core.service.domain.analysis.data.dto.response.DomainAnalysisDto
import team.exit_1.repo.backend.core.service.global.common.error.exception.ExpectedException
import team.exit_1.repo.backend.core.service.global.thirdparty.client.LlmServiceClient
import team.exit_1.repo.backend.core.service.global.thirdparty.data.request.AnalysisRequest
import team.exit_1.repo.backend.core.service.global.thirdparty.data.response.AnalysisResponse

@Service
class GenerateAnalysisService(
    private val llmServiceClient: LlmServiceClient,
    private val objectMapper: ObjectMapper,
) {
    fun execute(userId: String): AnalysisResultResponse {
        val llmResponse = llmServiceClient.generateAnalysis(AnalysisRequest(userId = userId))

        if (!llmResponse.success || llmResponse.data == null) {
            throw ExpectedException(
                message = "LLM 서버에서 분석 생성에 실패했습니다: ${llmResponse.error?.message}",
                statusCode = HttpStatus.INTERNAL_SERVER_ERROR,
            )
        }

        val analysisResponse = objectMapper.convertValue(llmResponse.data, AnalysisResponse::class.java)

        return AnalysisResultResponse(
            userId = analysisResponse.userId,
            domains =
                analysisResponse.domains.map { domain ->
                    DomainAnalysisDto(
                        domain = domain.domain,
                        score = domain.score,
                        insights = domain.insights,
                        analysis = domain.analysis,
                    )
                },
            report = analysisResponse.report,
            analyzedAt = analysisResponse.analyzedAt,
        )
    }
}
