package team.exit_1.repo.backend.core.service.domain.service

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.exit_1.repo.backend.core.service.domain.data.repository.ConversationJpaRepository
import team.exit_1.repo.backend.core.service.global.common.error.exception.ExpectedException

@Service
class DeleteConversationService(
    private val conversationJpaRepository: ConversationJpaRepository
) {
    @Transactional
    fun execute(conversationId: String) {
        if (conversationJpaRepository.existsById(conversationId).not()) {
            throw ExpectedException(message = "대화가 존재하지 않습니다.", statusCode = HttpStatus.NOT_FOUND)
        }
        conversationJpaRepository.deleteById(conversationId)
    }
}