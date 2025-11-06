package team.exit_1.repo.backend.core.service.domain.profile.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.exit_1.repo.backend.core.service.domain.profile.data.dto.request.CreatePersonalInfoRequest
import team.exit_1.repo.backend.core.service.domain.profile.data.dto.response.PersonalInfoResponse
import team.exit_1.repo.backend.core.service.global.common.error.exception.ExpectedException
import team.exit_1.repo.backend.core.service.global.config.MockDataConfig
import team.exit_1.repo.backend.core.service.global.thirdparty.client.RagServiceClient
import team.exit_1.repo.backend.core.service.global.thirdparty.data.request.PersonalInfoCreateRequest as RagPersonalInfoCreateRequest

@Service
class CreatePersonalInfoService(
    private val ragServiceClient: RagServiceClient,
    private val objectMapper: ObjectMapper
) {
    @Transactional
    fun execute(request: CreatePersonalInfoRequest): PersonalInfoResponse {
        val userId = MockDataConfig.MOCK_USER_ID

        val ragRequest = RagPersonalInfoCreateRequest(
            userId = userId,
            content = request.content,
            category = request.category,
            importance = request.importance
        )

        val llmResponse = ragServiceClient.createPersonalInfo(ragRequest)

        if (!llmResponse.success || llmResponse.data == null) {
            throw ExpectedException(
                message = "RAG 서버에서 개인정보 생성에 실패했습니다: ${llmResponse.error?.message}",
                statusCode = HttpStatus.INTERNAL_SERVER_ERROR
            )
        }

        val dataMap = llmResponse.data as? Map<*, *>
            ?: throw ExpectedException(
                message = "RAG 서버 응답 형식이 올바르지 않습니다",
                statusCode = HttpStatus.INTERNAL_SERVER_ERROR
            )

        val personalInfoData = dataMap["personal_info"]
            ?: throw ExpectedException(
                message = "RAG 서버 응답에 personal_info가 없습니다",
                statusCode = HttpStatus.INTERNAL_SERVER_ERROR
            )

        return objectMapper.convertValue(personalInfoData, PersonalInfoResponse::class.java)
    }
}