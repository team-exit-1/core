package team.exit_1.repo.backend.core.service.global.thirdparty.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import team.exit_1.repo.backend.core.service.global.thirdparty.data.request.GameQuestionRequest
import team.exit_1.repo.backend.core.service.global.thirdparty.data.request.GameResultRequest
import team.exit_1.repo.backend.core.service.global.thirdparty.data.response.LlmApiResponse

@FeignClient(name = "llm-service", url = "\${llm-service.url}")
interface LlmServiceClient {

    @GetMapping("/health")
    fun healthCheck(): Map<String, Any>

    @PostMapping("/chat")
    fun chat(@RequestBody request: Map<String, Any>): Map<String, Any>

    @PostMapping("/generate")
    fun generate(@RequestBody request: Map<String, Any>): Map<String, Any>

    @PostMapping("/api/game/question")
    fun generateGameQuestion(@RequestBody request: GameQuestionRequest): LlmApiResponse

    @PostMapping("/api/game/result")
    fun evaluateGameResult(@RequestBody request: GameResultRequest): LlmApiResponse
}