package team.exit_1.repo.backend.core.service.domain.message.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import team.exit_1.repo.backend.core.service.domain.message.data.dto.request.SendMessageRequest
import team.exit_1.repo.backend.core.service.domain.message.data.dto.response.MessageListResponse
import team.exit_1.repo.backend.core.service.domain.message.data.dto.response.MessageResponse
import team.exit_1.repo.backend.core.service.domain.message.service.QueryMessagesService
import team.exit_1.repo.backend.core.service.domain.message.service.SendMessageService
import team.exit_1.repo.backend.core.service.global.common.response.data.reponse.CommonApiResponse

@RestController
@Tag(name = "메시지 API", description = "대화 메시지 관련 API입니다.")
@RequestMapping("/v1/conversations/{conversationId}/messages")
class MessageController(
    private val queryMessagesService: QueryMessagesService,
    private val sendMessageService: SendMessageService,
) {
    @GetMapping
    @Operation(summary = "메시지 목록 조회", description = "특정 대화의 모든 메시지(사용자 메시지 + AI 응답)를 시간순으로 조회합니다.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "메시지 목록이 성공적으로 조회되었습니다.",
            ),
            ApiResponse(
                responseCode = "404",
                description = "대화를 찾을 수 없습니다.",
                content = [Content()],
            ),
        ],
    )
    fun getMessages(
        @Parameter(description = "대화 ID", example = "conv_550e8400-e29b-41d4-a716-446655440000")
        @PathVariable conversationId: String,
    ): CommonApiResponse<List<MessageListResponse>> =
        CommonApiResponse.success("메시지 목록이 성공적으로 조회되었습니다", queryMessagesService.execute(conversationId))

    @PostMapping
    @Operation(
        summary = "메시지 전송 및 AI 응답 받기",
        description = "특정 대화에 새로운 메시지를 전송하고 LLM 서버로부터 AI 응답을 받습니다. 사용자 메시지와 AI 응답이 모두 저장되며, AI 응답이 반환됩니다.",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "메시지가 성공적으로 전송되고 AI 응답이 반환되었습니다.",
            ),
            ApiResponse(
                responseCode = "404",
                description = "대화를 찾을 수 없습니다.",
                content = [Content()],
            ),
            ApiResponse(
                responseCode = "500",
                description = "LLM 서버 응답 실패 또는 내부 오류가 발생했습니다.",
                content = [Content()],
            ),
        ],
    )
    fun sendMessage(
        @Parameter(description = "대화 ID", example = "conv_550e8400-e29b-41d4-a716-446655440000")
        @PathVariable conversationId: String,
        @RequestBody request: SendMessageRequest,
    ): CommonApiResponse<MessageResponse> =
        CommonApiResponse.created("메시지가 성공적으로 전송되고 AI 응답이 반환되었습니다", sendMessageService.execute(conversationId, request))
}
