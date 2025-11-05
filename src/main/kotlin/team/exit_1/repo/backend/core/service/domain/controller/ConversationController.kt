package team.exit_1.repo.backend.core.service.domain.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "대화형 서비스 API", description = "대화형 서비스 관련 API입니다.")
@RequestMapping("/conversations")
class ConversationController {

    @PostMapping
    @Operation(summary = "대화 생성", description = "새로운 대화를 생성합니다.")
    @ApiResponse(responseCode = "201")
    fun createConversation() {

    }
}