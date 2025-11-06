package team.exit_1.repo.backend.core.service.domain.game.service

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.exit_1.repo.backend.core.service.domain.game.data.constant.GameSessionStatus
import team.exit_1.repo.backend.core.service.domain.game.data.dto.response.GameSessionResponse
import team.exit_1.repo.backend.core.service.domain.game.data.repository.GameSessionJpaRepository
import team.exit_1.repo.backend.core.service.global.common.error.exception.ExpectedException
import java.time.LocalDateTime

@Service
class EndGameSessionService(
    private val gameSessionJpaRepository: GameSessionJpaRepository
) {
    @Transactional
    fun execute(sessionId: String): GameSessionResponse {
        val gameSession = gameSessionJpaRepository.findById(sessionId)
            .orElseThrow { ExpectedException(message = "게임 세션이 존재하지 않습니다.", statusCode = HttpStatus.NOT_FOUND) }

        if (gameSession.status == GameSessionStatus.COMPLETED) {
            throw ExpectedException(
                message = "이미 종료된 게임 세션입니다.",
                statusCode = HttpStatus.CONFLICT
            )
        }

        gameSession.status = GameSessionStatus.COMPLETED
        gameSession.endTime = LocalDateTime.now()

        val savedSession = gameSessionJpaRepository.save(gameSession)

        return GameSessionResponse(
            sessionId = savedSession.id!!,
            userId = savedSession.userId!!,
            status = savedSession.status,
            startTime = savedSession.startTime!!,
            endTime = savedSession.endTime,
            totalScore = savedSession.totalScore,
            currentDifficulty = savedSession.currentDifficulty
        )
    }
}