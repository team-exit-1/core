package team.exit_1.repo.backend.core.service.domain.profile.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.exit_1.repo.backend.core.service.domain.profile.data.dto.response.PersonalInfoListResponse
import team.exit_1.repo.backend.core.service.global.common.error.exception.ExpectedException
import team.exit_1.repo.backend.core.service.global.config.MockDataConfig
import team.exit_1.repo.backend.core.service.global.thirdparty.client.RagServiceClient

@Service
class QueryPersonalInfoService(
    private val ragServiceClient: RagServiceClient,
    private val objectMapper: ObjectMapper,
) {
    @Transactional(readOnly = true)
    fun execute(): PersonalInfoListResponse {
        val userId = MockDataConfig.MOCK_USER_ID

        val llmResponse = ragServiceClient.getPersonalInfoByUser(userId)

        if (!llmResponse.success || llmResponse.data == null) {
            throw ExpectedException(
                message = "RAG 서버에서 개인정보 조회에 실패했습니다: ${llmResponse.error?.message}",
                statusCode = HttpStatus.INTERNAL_SERVER_ERROR,
            )
        }

        val dataMap =
            llmResponse.data as? Map<*, *>
                ?: throw ExpectedException(
                    message = "RAG 서버 응답 형식이 올바르지 않습니다",
                    statusCode = HttpStatus.INTERNAL_SERVER_ERROR,
                )

        val personalInfoListData =
            dataMap["personal_info_list"]
                ?: throw ExpectedException(
                    message = "RAG 서버 응답에 personal_info_list가 없습니다",
                    statusCode = HttpStatus.INTERNAL_SERVER_ERROR,
                )

        return objectMapper.convertValue(personalInfoListData, PersonalInfoListResponse::class.java)
    }
}
