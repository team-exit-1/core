package team.exit_1.repo.backend.core.service.global.thirdparty.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import team.exit_1.repo.backend.core.service.global.thirdparty.data.request.StoreConversationRequest
import team.exit_1.repo.backend.core.service.global.thirdparty.data.response.StoreConversationResponse

@FeignClient(name = "rag-service", url = "\${rag-service.url}")
interface RagServiceClient {

    @GetMapping("/api/rag/health")
    fun healthCheck(): Map<String, Any>

    @PostMapping("/api/rag/conversation/store")
    fun storeConversation(@RequestBody request: StoreConversationRequest): StoreConversationResponse
}