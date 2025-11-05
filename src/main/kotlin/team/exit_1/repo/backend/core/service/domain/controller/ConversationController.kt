package team.exit_1.repo.backend.core.service.domain.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import team.exit_1.repo.backend.core.service.domain.data.dto.response.CreateConversationResponse
import team.exit_1.repo.backend.core.service.domain.service.CreateConversationService
import team.exit_1.repo.backend.core.service.global.common.error.data.response.ErrorResponse

@RestController
@Tag(name = "대화형 서비스 API", description = "대화형 서비스 관련 API입니다.")
@RequestMapping("/conversations")
class ConversationController(
    private val createConversationService: CreateConversationService,
) {

    @PostMapping
    @Operation(summary = "대화 생성", description = "새로운 대화를 생성합니다.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "대화가 성공적으로 생성되었습니다.",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = CreateConversationResponse::class))]
            ),
            ApiResponse(
                responseCode = "409",
                description = "이미 대화가 존재합니다.",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @ResponseStatus(HttpStatus.CREATED)
    fun createConversation(): CreateConversationResponse {
        return createConversationService.execute()
    }

    @DeleteMapping
    @Operation(summary = "대화 삭제", description = "기존 대화를 삭제합니다.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "204",
                description = "대화가 성공적으로 삭제되었습니다."
            ),
            ApiResponse(
                responseCode = "404",
                description = "대화를 찾을 수 없습니다.",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteConversation() {
}