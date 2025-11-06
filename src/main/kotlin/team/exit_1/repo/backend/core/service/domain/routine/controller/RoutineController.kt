package team.exit_1.repo.backend.core.service.domain.routine.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import team.exit_1.repo.backend.core.service.domain.routine.data.dto.request.CreateRoutineRequest
import team.exit_1.repo.backend.core.service.domain.routine.data.dto.response.RoutineListResponse
import team.exit_1.repo.backend.core.service.domain.routine.data.dto.response.RoutineResponse
import team.exit_1.repo.backend.core.service.domain.routine.service.CreateRoutineService
import team.exit_1.repo.backend.core.service.domain.routine.service.DeleteRoutineService
import team.exit_1.repo.backend.core.service.domain.routine.service.QueryRoutinesService
import team.exit_1.repo.backend.core.service.global.common.response.data.reponse.CommonApiResponse

@RestController
@Tag(name = "루틴 API", description = "일상 루틴 관리 API입니다.")
@RequestMapping("/v1/routines")
class RoutineController(
    private val createRoutineService: CreateRoutineService,
    private val queryRoutinesService: QueryRoutinesService,
    private val deleteRoutineService: DeleteRoutineService,
) {
    @PostMapping
    @Operation(summary = "루틴 생성", description = "새로운 일상 루틴을 등록합니다. 제목, 내용, 알림 시각을 설정할 수 있습니다.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "루틴이 성공적으로 생성되었습니다.",
            ),
            ApiResponse(
                responseCode = "400",
                description = "잘못된 요청입니다.",
                content = [Content()],
            ),
        ],
    )
    fun createRoutine(
        @RequestBody request: CreateRoutineRequest,
    ): CommonApiResponse<RoutineResponse> =
        CommonApiResponse.created(
            "루틴이 성공적으로 생성되었습니다",
            createRoutineService.execute(request),
        )

    @GetMapping
    @Operation(summary = "루틴 목록 조회", description = "현재 사용자의 모든 루틴을 조회합니다.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "루틴 목록이 성공적으로 조회되었습니다.",
            ),
        ],
    )
    fun getRoutines(): CommonApiResponse<RoutineListResponse> =
        CommonApiResponse.success(
            "루틴 목록이 성공적으로 조회되었습니다",
            queryRoutinesService.execute(),
        )

    @DeleteMapping("/{routineId}")
    @Operation(summary = "루틴 삭제", description = "등록된 루틴을 삭제합니다.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "루틴이 성공적으로 삭제되었습니다.",
            ),
            ApiResponse(
                responseCode = "404",
                description = "루틴을 찾을 수 없습니다.",
                content = [Content()],
            ),
        ],
    )
    fun deleteRoutine(
        @Parameter(description = "루틴 ID", example = "1")
        @PathVariable routineId: Long,
    ): CommonApiResponse<Nothing> {
        deleteRoutineService.execute(routineId)
        return CommonApiResponse.success("루틴이 성공적으로 삭제되었습니다")
    }
}
