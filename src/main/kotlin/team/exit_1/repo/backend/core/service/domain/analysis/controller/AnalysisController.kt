package team.exit_1.repo.backend.core.service.domain.analysis.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import team.exit_1.repo.backend.core.service.domain.analysis.data.dto.response.AnalysisResultResponse
import team.exit_1.repo.backend.core.service.domain.analysis.service.GenerateAnalysisService
import team.exit_1.repo.backend.core.service.global.common.response.data.reponse.CommonApiResponse

@RestController
@Tag(name = "분석 API", description = "사용자의 대화 기록과 퀴즈 결과를 분석하여 보고서를 생성합니다.")
@RequestMapping("/v1")
class AnalysisController(
    private val generateAnalysisService: GenerateAnalysisService,
) {
    @GetMapping("/users/{userId}/analysis")
    @Operation(summary = "사용자 분석 보고서 생성", description = "사용자의 대화 기록과 틀린 퀴즈를 분석하여 4개 도메인(가족, 생애 사건, 직업/경력, 취미/관심사)에 대한 보고서를 생성합니다.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "분석 보고서가 성공적으로 생성되었습니다.",
            ),
            ApiResponse(
                responseCode = "500",
                description = "LLM 서버에서 분석 생성에 실패했습니다.",
                content = [Content()],
            ),
        ],
    )
    fun generateAnalysis(
        @Parameter(description = "사용자 ID", example = "user_2419")
        @PathVariable userId: String,
    ): CommonApiResponse<AnalysisResultResponse> =
        CommonApiResponse.success(
            "분석 보고서가 성공적으로 생성되었습니다",
            generateAnalysisService.execute(userId),
        )
}