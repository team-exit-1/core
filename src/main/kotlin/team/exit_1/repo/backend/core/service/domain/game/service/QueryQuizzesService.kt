package team.exit_1.repo.backend.core.service.domain.game.service

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.exit_1.repo.backend.core.service.domain.game.data.dto.response.QuizResponse
import team.exit_1.repo.backend.core.service.domain.game.data.repository.GameSessionJpaRepository
import team.exit_1.repo.backend.core.service.domain.game.data.repository.QuizJpaRepository
import team.exit_1.repo.backend.core.service.global.common.error.exception.ExpectedException

@Service
class QueryQuizzesService(
    private val gameSessionJpaRepository: GameSessionJpaRepository,
    private val quizJpaRepository: QuizJpaRepository
) {
    @Transactional(readOnly = true)
    fun execute(sessionId: String): List<QuizResponse> {
        val gameSession = gameSessionJpaRepository.findById(sessionId)
            .orElseThrow { ExpectedException(message = "게임 세션이 존재하지 않습니다.", statusCode = HttpStatus.NOT_FOUND) }

        // 현재 세션의 난이도에 맞는 퀴즈 조회
        return quizJpaRepository.findAllByDifficulty(gameSession.currentDifficulty)
            .map { quiz ->
                QuizResponse(
                    quizId = quiz.id!!,
                    question = quiz.question!!,
                    difficulty = quiz.difficulty,
                    category = quiz.category,
                    hint = quiz.hint
                )
            }
    }
}
