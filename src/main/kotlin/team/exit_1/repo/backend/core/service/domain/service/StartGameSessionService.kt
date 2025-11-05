package team.exit_1.repo.backend.core.service.domain.service

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.exit_1.repo.backend.core.service.domain.data.constant.GameSessionStatus
import team.exit_1.repo.backend.core.service.domain.data.constant.QuizDifficulty
import team.exit_1.repo.backend.core.service.domain.data.dto.response.GameSessionResponse
import team.exit_1.repo.backend.core.service.domain.data.entity.GameSession
import team.exit_1.repo.backend.core.service.domain.data.repository.ConversationJpaRepository
import team.exit_1.repo.backend.core.service.domain.data.repository.GameSessionJpaRepository
import team.exit_1.repo.backend.core.service.global.common.error.exception.ExpectedException
import java.time.LocalDateTime
import java.util.*

@Service
class StartGameSessionService(
    private val conversationJpaRepository: ConversationJpaRepository,
    private val gameSessionJpaRepository: GameSessionJpaRepository
) {
    @Transactional
    fun execute(conversationId: String): GameSessionResponse {
        val conversation = conversationJpaRepository.findById(conversationId)
            .orElseThrow { ExpectedException(message = "대화가 존재하지 않습니다.", statusCode = HttpStatus.NOT_FOUND) }

        // 이미 진행중인 세션이 있는지 확인
        val existingSession = gameSessionJpaRepository.findByConversationAndStatus(conversation, GameSessionStatus.IN_PROGRESS)
        if (existingSession != null) {
            return GameSessionResponse(
                sessionId = existingSession.id!!,
                conversationId = conversation.id!!,
                status = existingSession.status,
                startTime = existingSession.startTime!!,
                endTime = existingSession.endTime,
                totalScore = existingSession.totalScore,
                currentDifficulty = existingSession.currentDifficulty
            )
        }

        val gameSession = GameSession().apply {
            this.id = "session_${UUID.randomUUID()}"
            this.conversation = conversation
            this.status = GameSessionStatus.IN_PROGRESS
            this.startTime = LocalDateTime.now()
            this.totalScore = 0
            this.currentDifficulty = QuizDifficulty.EASY
        }

        val savedSession = gameSessionJpaRepository.save(gameSession)

        return GameSessionResponse(
            sessionId = savedSession.id!!,
            conversationId = conversation.id!!,
            status = savedSession.status,
            startTime = savedSession.startTime!!,
            endTime = savedSession.endTime,
            totalScore = savedSession.totalScore,
            currentDifficulty = savedSession.currentDifficulty
        )
    }
}
