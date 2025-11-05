package team.exit_1.repo.backend.core.service.domain.service

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import team.exit_1.repo.backend.core.service.domain.data.repository.ConversationJpaRepository
import team.exit_1.repo.backend.core.service.global.common.error.exception.ExpectedException
import team.exit_1.repo.backend.core.service.global.config.MockDataConfig.Companion.MOCK_CONVERSATION_ID

@Service
class DeleteConversationService(
    private val conversationJpaRepository: ConversationJpaRepository
) {
    fun execute() {
        if (conversationJpaRepository.existsById(MOCK_CONVERSATION_ID).not()) {
            throw ExpectedException(message = "대화가 존재하지 않습니다.", statusCode = HttpStatus.NOT_FOUND)
        }
        conversationJpaRepository.deleteById(MOCK_CONVERSATION_ID)
    }
}