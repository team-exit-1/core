package team.exit_1.repo.backend.core.service.domain.game.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import team.exit_1.repo.backend.core.service.domain.game.data.dto.request.SubmitQuizAnswerRequest
import team.exit_1.repo.backend.core.service.domain.game.data.dto.response.GameProgressResponse
import team.exit_1.repo.backend.core.service.domain.game.data.dto.response.GameSessionResponse
import team.exit_1.repo.backend.core.service.domain.game.data.dto.response.QuizAttemptResponse
import team.exit_1.repo.backend.core.service.domain.game.data.dto.response.QuizResponse
import team.exit_1.repo.backend.core.service.domain.game.service.*
import team.exit_1.repo.backend.core.service.global.common.response.data.reponse.CommonApiResponse

@RestController
@Tag(name = "게임 API", description = "게이미피케이션 기반 기억 리마인딩 API입니다.")
@RequestMapping("/v1")
class GameController(
    private val startGameSessionService: StartGameSessionService,
    private val queryQuizzesService: QueryQuizzesService,
    private val submitQuizAnswerService: SubmitQuizAnswerService,
    private val queryGameProgressService: QueryGameProgressService
) {

    @PostMapping("/conversations/{conversationId}/game-sessions")
    @Operation(summary = "게임 세션 시작", description = "특정 대화에서 새로운 게임 세션을 시작합니다.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "게임 세션이 성공적으로 시작되었습니다."
            ),
            ApiResponse(
                responseCode = "404",
                description = "대화를 찾을 수 없습니다."
            )
        ]
    )
    fun startGameSession(
        @Parameter(description = "대화 ID", example = "conv_550e8400-e29b-41d4-a716-446655440000")
        @PathVariable conversationId: String
    ): CommonApiResponse<GameSessionResponse> {
        return CommonApiResponse.created("게임 세션이 성공적으로 시작되었습니다", startGameSessionService.execute(conversationId))
    }

    @GetMapping("/game-sessions/{sessionId}/quizzes")
    @Operation(summary = "퀴즈 목록 조회", description = "게임 세션의 현재 난이도에 맞는 퀴즈 목록을 조회합니다.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "퀴즈 목록이 성공적으로 조회되었습니다."
            ),
            ApiResponse(
                responseCode = "404",
                description = "게임 세션을 찾을 수 없습니다."
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
                description = "게임 세션 또는 퀴즈를 찾을 수 없습니다."
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
                description = "게임 세션을 찾을 수 없습니다."
            )
        ]
    )
    fun getGameProgress(
        @Parameter(description = "세션 ID", example = "session_550e8400-e29b-41d4-a716-446655440000")
        @PathVariable sessionId: String
    ): CommonApiResponse<GameProgressResponse> {
        return CommonApiResponse.success("진행도가 성공적으로 조회되었습니다", queryGameProgressService.execute(sessionId))
    }
}
