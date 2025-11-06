package team.exit_1.repo.backend.core.service.domain.profile.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import team.exit_1.repo.backend.core.service.domain.profile.data.dto.request.CreatePersonalInfoRequest
import team.exit_1.repo.backend.core.service.domain.profile.data.dto.response.PersonalInfoListResponse
import team.exit_1.repo.backend.core.service.domain.profile.data.dto.response.PersonalInfoResponse
import team.exit_1.repo.backend.core.service.domain.profile.service.CreatePersonalInfoService
import team.exit_1.repo.backend.core.service.domain.profile.service.QueryPersonalInfoService
import team.exit_1.repo.backend.core.service.global.common.response.data.reponse.CommonApiResponse

@RestController
@Tag(name = "프로필 API", description = "사용자 개인정보 관리 API입니다.")
@RequestMapping("/v1/profile")
class ProfileController(
    private val createPersonalInfoService: CreatePersonalInfoService,
    private val queryPersonalInfoService: QueryPersonalInfoService
) {

    @PostMapping
    @Operation(summary = "개인정보 생성", description = "보호자가 입력한 개인정보를 저장합니다. 의료정보, 연락처, 비상연락처, 알레르기 정보 등을 저장할 수 있습니다.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "개인정보가 성공적으로 생성되었습니다."
            ),
            ApiResponse(
                responseCode = "400",
                description = "잘못된 요청입니다.",
                content = [Content()]
            ),
            ApiResponse(
                responseCode = "500",
                description = "RAG 서버에서 개인정보 생성에 실패했습니다.",
                content = [Content()]
            )
        ]
    )
    fun createPersonalInfo(
        @RequestBody request: CreatePersonalInfoRequest
    ): CommonApiResponse<PersonalInfoResponse> {
        return CommonApiResponse.created(
            "개인정보가 성공적으로 생성되었습니다",
            createPersonalInfoService.execute(request)
        )
    }

    @GetMapping
    @Operation(summary = "개인정보 목록 조회", description = "현재 사용자의 모든 개인정보를 조회합니다.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "개인정보 목록이 성공적으로 조회되었습니다."
            ),
            ApiResponse(
                responseCode = "500",
                description = "RAG 서버에서 개인정보 조회에 실패했습니다.",
                content = [Content()]
            )
        ]
    )
    fun getPersonalInfo(): CommonApiResponse<PersonalInfoListResponse> {
        return CommonApiResponse.success(
            "개인정보 목록이 성공적으로 조회되었습니다",
            queryPersonalInfoService.execute()
        )
    }
}