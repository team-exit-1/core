package team.exit_1.repo.backend.core.service.domain.game.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.exit_1.repo.backend.core.service.domain.game.data.constant.GameSessionStatus
import team.exit_1.repo.backend.core.service.domain.game.data.constant.QuizDifficulty
import team.exit_1.repo.backend.core.service.domain.game.data.dto.response.GameSessionResponse
import team.exit_1.repo.backend.core.service.domain.game.data.entity.GameSession
import team.exit_1.repo.backend.core.service.domain.game.data.repository.GameSessionJpaRepository
import team.exit_1.repo.backend.core.service.global.config.MockDataConfig
import java.time.LocalDateTime
import java.util.UUID

@Service
class StartGameSessionService(
    private val gameSessionJpaRepository: GameSessionJpaRepository,
) {
    @Transactional
    fun execute(): GameSessionResponse {
        val userId = MockDataConfig.MOCK_USER_ID

        val existingSession = gameSessionJpaRepository.findByUserIdAndStatus(userId, GameSessionStatus.IN_PROGRESS)
        if (existingSession != null) {
            return GameSessionResponse(
                sessionId = existingSession.id!!,
                userId = existingSession.userId!!,
                status = existingSession.status,
                startTime = existingSession.startTime!!,
                endTime = existingSession.endTime,
                totalScore = existingSession.totalScore,
                currentDifficulty = existingSession.currentDifficulty,
            )
        }

        val gameSession =
            GameSession().apply {
                this.id = "session_${UUID.randomUUID()}"
                this.userId = userId
                this.status = GameSessionStatus.IN_PROGRESS
                this.startTime = LocalDateTime.now()
                this.totalScore = 0
                this.currentDifficulty = QuizDifficulty.EASY
            }

        val savedSession = gameSessionJpaRepository.save(gameSession)

        return GameSessionResponse(
            sessionId = savedSession.id!!,
            userId = savedSession.userId!!,
            status = savedSession.status,
            startTime = savedSession.startTime!!,
            endTime = savedSession.endTime,
            totalScore = savedSession.totalScore,
            currentDifficulty = savedSession.currentDifficulty,
        )
    }
}
