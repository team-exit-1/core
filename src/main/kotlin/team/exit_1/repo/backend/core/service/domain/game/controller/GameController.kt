package team.exit_1.repo.backend.core.service.domain.game.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import team.exit_1.repo.backend.core.service.domain.game.data.dto.request.SubmitQuizAnswerRequest
import team.exit_1.repo.backend.core.service.domain.game.data.dto.response.*
import team.exit_1.repo.backend.core.service.domain.game.service.*
import team.exit_1.repo.backend.core.service.global.common.response.data.reponse.CommonApiResponse

@RestController
@Tag(name = "게임 API", description = "게이미피케이션 기반 기억 리마인딩 API입니다.")
@RequestMapping("/v1")
class GameController(
    private val startGameSessionService: StartGameSessionService,
    private val queryQuizzesService: QueryQuizzesService,
    private val submitQuizAnswerService: SubmitQuizAnswerService,
    private val queryGameProgressService: QueryGameProgressService,
    private val endGameSessionService: EndGameSessionService,
    private val queryCompletedGameSessionService: QueryCompletedGameSessionService
) {

    @PostMapping("/game-sessions")
    @Operation(summary = "게임 세션 시작", description = "사용자의 새로운 게임 세션을 시작합니다.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "게임 세션이 성공적으로 시작되었습니다."
            )
        ]
    )
    fun startGameSession(): CommonApiResponse<GameSessionResponse> {
        return CommonApiResponse.created("게임 세션이 성공적으로 시작되었습니다", startGameSessionService.execute())
    }

    @GetMapping("/game-sessions/{sessionId}/quizzes")
    @Operation(summary = "퀴즈 목록 조회", description = "게임 세션의 현재 난이도에 맞는 퀴즈를 LLM을 통해 생성하고 조회합니다.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "퀴즈 목록이 성공적으로 조회되었습니다."
            ),
            ApiResponse(
                responseCode = "404",
                description = "게임 세션 또는 사용자 정보를 찾을 수 없습니다.",
                content = [Content()]
            ),
            ApiResponse(
                responseCode = "409",
                description = "이미 종료된 게임 세션입니다.",
                content = [Content()]
            ),
            ApiResponse(
                responseCode = "410",
                description = "게임 세션이 시간 제한을 초과하여 자동 종료되었습니다.",
                content = [Content()]
            ),
            ApiResponse(
                responseCode = "500",
                description = "LLM 서버에서 질문 생성에 실패했습니다.",
                content = [Content()]
            )
        ]
    )
    fun getQuizzes(
        @Parameter(description = "세션 ID", example = "session_550e8400-e29b-41d4-a716-446655440000")
        @PathVariable sessionId: String
    ): CommonApiResponse<List<QuizResponse>> {
        return CommonApiResponse.success("퀴즈 목록이 성공적으로 조회되었습니다", queryQuizzesService.execute(sessionId))
    }

    @PostMapping("/game-sessions/{sessionId}/quiz-attempts")
    @Operation(summary = "퀴즈 답변 제출", description = "퀴즈에 대한 답변을 제출합니다.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "답변이 성공적으로 제출되었습니다."
            ),
            ApiResponse(
                responseCode = "404",
                description = "게임 세션 또는 퀴즈를 찾을 수 없습니다.",
                content = [Content()]
            ),
            ApiResponse(
                responseCode = "409",
                description = "이미 답변이 제출된 퀴즈입니다.",
                content = [Content()]
            )
        ]
    )
    fun submitQuizAnswer(
        @Parameter(description = "세션 ID", example = "session_550e8400-e29b-41d4-a716-446655440000")
        @PathVariable sessionId: String,
        @RequestBody request: SubmitQuizAnswerRequest
    ): CommonApiResponse<QuizAttemptResponse> {
        return CommonApiResponse.created("답변이 성공적으로 제출되었습니다", submitQuizAnswerService.execute(sessionId, request))
    }

    @GetMapping("/game-sessions/{sessionId}/progress")
    @Operation(summary = "게임 진행도 조회", description = "게임 세션의 진행도를 조회합니다.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "진행도가 성공적으로 조회되었습니다."
            ),
            ApiResponse(
                responseCode = "404",
                description = "게임 세션을 찾을 수 없습니다.",
                content = [Content()]
            ),
            ApiResponse(
                responseCode = "409",
                description = "이미 종료된 게임 세션입니다.",
                content = [Content()]
            ),
            ApiResponse(
                responseCode = "410",
                description = "게임 세션이 시간 제한을 초과하여 자동 종료되었습니다.",
                content = [Content()]
            )
        ]
    )
    fun getGameProgress(
        @Parameter(description = "세션 ID", example = "session_550e8400-e29b-41d4-a716-446655440000")
        @PathVariable sessionId: String
    ): CommonApiResponse<GameProgressResponse> {
        return CommonApiResponse.success("진행도가 성공적으로 조회되었습니다", queryGameProgressService.execute(sessionId))
    }

    @PostMapping("/game-sessions/{sessionId}/end")
    @Operation(summary = "게임 세션 종료", description = "진행 중인 게임 세션을 종료합니다.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "게임 세션이 성공적으로 종료되었습니다."
            ),
            ApiResponse(
                responseCode = "404",
                description = "게임 세션을 찾을 수 없습니다.",
                content = [Content()]
            ),
            ApiResponse(
                responseCode = "409",
                description = "이미 종료된 게임 세션입니다.",
                content = [Content()]
            )
        ]
    )
    fun endGameSession(
        @Parameter(description = "세션 ID", example = "session_550e8400-e29b-41d4-a716-446655440000")
        @PathVariable sessionId: String
    ): CommonApiResponse<GameSessionResponse> {
        return CommonApiResponse.success("게임 세션이 성공적으로 종료되었습니다", endGameSessionService.execute(sessionId))
    }

    @GetMapping("/game-sessions/{sessionId}/completed")
    @Operation(summary = "완료된 게임 세션 상세 조회", description = "완료된 게임 세션의 모든 정보를 조회합니다. 출제된 문제들, 사용자 답안, 정답률, 통계 등 전체 정보를 포함합니다.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "완료된 게임 세션 정보가 성공적으로 조회되었습니다."
            ),
            ApiResponse(
                responseCode = "400",
                description = "아직 완료되지 않은 게임 세션입니다.",
                content = [Content()]
            ),
            ApiResponse(
                responseCode = "404",
                description = "게임 세션을 찾을 수 없습니다.",
                content = [Content()]
            )
        ]
    )
    fun getCompletedGameSession(
        @Parameter(description = "세션 ID", example = "session_550e8400-e29b-41d4-a716-446655440000")
        @PathVariable sessionId: String
    ): CommonApiResponse<CompletedGameSessionDetailResponse> {
        return CommonApiResponse.success(
            "완료된 게임 세션 정보가 성공적으로 조회되었습니다",
            queryCompletedGameSessionService.execute(sessionId)
        )
    }
}
