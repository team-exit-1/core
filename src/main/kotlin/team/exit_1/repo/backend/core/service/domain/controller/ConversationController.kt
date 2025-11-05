package team.exit_1.repo.backend.core.service.domain.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import team.exit_1.repo.backend.core.service.domain.data.dto.response.ConversationResponse
import team.exit_1.repo.backend.core.service.domain.service.CreateConversationService
import team.exit_1.repo.backend.core.service.domain.service.DeleteConversationService
import team.exit_1.repo.backend.core.service.domain.service.DisableConversationService
import team.exit_1.repo.backend.core.service.global.common.response.data.reponse.CommonApiResponse

@RestController
@Tag(name = "대화형 서비스 API", description = "대화형 서비스 관련 API입니다.")
@RequestMapping("/conversations")
class ConversationController(
    private val createConversationService: CreateConversationService,
    private val deleteConversationService: DeleteConversationService,
    private val disableConversationService: DisableConversationService
) {

    @PostMapping
    @Operation(summary = "대화 생성", description = "새로운 대화를 생성합니다. 대화 ID는 자동으로 생성됩니다.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "대화가 성공적으로 생성되었습니다.",
                content = [Content()]
            )
        ]
    )
    @ResponseStatus(HttpStatus.CREATED)
    fun createConversation(): ConversationResponse {
        return createConversationService.execute()
    }

    @DeleteMapping("/{conversationId}")
    @Operation(summary = "대화 삭제", description = "지정된 대화 ID의 대화를 삭제합니다.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "204",
                description = "대화가 성공적으로 삭제되었습니다."
            ),
            ApiResponse(
                responseCode = "404",
                description = "대화를 찾을 수 없습니다.",
                content = [Content()]
            )
        ]
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteConversation(
        @Parameter(description = "삭제할 대화 ID", example = "conv_550e8400-e29b-41d4-a716-446655440000")
        @PathVariable conversationId: String
    ) {
        deleteConversationService.execute(conversationId)
    }

    @PatchMapping("/{conversationId}/disable")
    @Operation(summary = "대화 종료", description = "지정된 대화 ID의 대화를 종료(비활성화)합니다.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "대화가 성공적으로 종료되었습니다."
            ),
            ApiResponse(
                responseCode = "404",
                description = "대화를 찾을 수 없습니다.",
                content = [Content()]
            )
        ]
    )
    fun disableConversation(
        @Parameter(description = "종료할 대화 ID", example = "conv_550e8400-e29b-41d4-a716-446655440000")
        @PathVariable conversationId: String
    ): ConversationResponse {
        return disableConversationService.execute(conversationId)
    }

    @GetMapping("/active")
    @Operation(summary = "활성 대화 목록 조회", description = "현재 활성 상태인 모든 대화의 목록을 조회합니다.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "활성 대화 목록이 성공적으로 조회되었습니다.",
            )
        ]
    )
    fun getActiveConversations(): List<ConversationResponse> {
        // 이 메서드는 활성 대화 목록을 반환하는 로직을 구현해야 합니다.
        // 예시로 빈 리스트를 반환하도록 작성했습니다.
        return emptyList()
    }
}