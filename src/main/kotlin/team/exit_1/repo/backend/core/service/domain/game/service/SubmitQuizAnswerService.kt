package team.exit_1.repo.backend.core.service.domain.game.service

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.exit_1.repo.backend.core.service.domain.game.data.constant.QuizDifficulty
import team.exit_1.repo.backend.core.service.domain.game.data.dto.request.SubmitQuizAnswerRequest
import team.exit_1.repo.backend.core.service.domain.game.data.dto.response.QuizAttemptResponse
import team.exit_1.repo.backend.core.service.domain.game.data.entity.GameSession
import team.exit_1.repo.backend.core.service.domain.game.data.entity.QuizAttempt
import team.exit_1.repo.backend.core.service.domain.game.data.repository.GameSessionJpaRepository
import team.exit_1.repo.backend.core.service.domain.game.data.repository.QuizAttemptJpaRepository
import team.exit_1.repo.backend.core.service.domain.game.data.repository.QuizJpaRepository
import team.exit_1.repo.backend.core.service.global.common.error.exception.ExpectedException
import java.time.LocalDateTime

@Service
class SubmitQuizAnswerService(
    private val gameSessionJpaRepository: GameSessionJpaRepository,
    private val quizJpaRepository: QuizJpaRepository,
    private val quizAttemptJpaRepository: QuizAttemptJpaRepository
) {
    @Transactional
    fun execute(sessionId: String, request: SubmitQuizAnswerRequest): QuizAttemptResponse {
        val gameSession = gameSessionJpaRepository.findById(sessionId)
            .orElseThrow { ExpectedException(message = "게임 세션이 존재하지 않습니다.", statusCode = HttpStatus.NOT_FOUND) }

        val quiz = quizJpaRepository.findById(request.quizId)
            .orElseThrow { ExpectedException(message = "퀴즈가 존재하지 않습니다.", statusCode = HttpStatus.NOT_FOUND) }

        // 정답 확인 (대소문자 구분 없이, 공백 제거)
        val isCorrect = quiz.correctAnswer?.trim()?.equals(request.answer.trim(), ignoreCase = true) ?: false

        // 점수 계산 (난이도별 점수)
        val score = if (isCorrect) {
            when (quiz.difficulty) {
                QuizDifficulty.EASY -> 10
                QuizDifficulty.MEDIUM -> 20
                QuizDifficulty.HARD -> 30
            }
        } else {
            0
        }

        // 시도 기록 저장
        val quizAttempt = QuizAttempt().apply {
            this.gameSession = gameSession
            this.quiz = quiz
            this.userAnswer = request.answer
            this.isCorrect = isCorrect
            this.score = score
            this.attemptTime = LocalDateTime.now()
        }

        val savedAttempt = quizAttemptJpaRepository.save(quizAttempt)

        // 게임 세션 점수 업데이트
        gameSession.totalScore += score
        
        // 난이도 자동 조절 (최근 5문제 기준으로 정답률 80% 이상이면 난이도 상승)
        adjustDifficulty(gameSession)
        
        gameSessionJpaRepository.save(gameSession)

        return QuizAttemptResponse(
            attemptId = savedAttempt.id!!,
            quizId = quiz.id!!,
            userAnswer = savedAttempt.userAnswer!!,
            isCorrect = savedAttempt.isCorrect,
            score = savedAttempt.score,
            attemptTime = savedAttempt.attemptTime!!
        )
    }

    private fun adjustDifficulty(gameSession: GameSession) {
        val recentAttempts = quizAttemptJpaRepository.findAllByGameSession(gameSession)
            .sortedByDescending { it.attemptTime }
            .take(5)

        if (recentAttempts.size < 5) return

        val correctCount = recentAttempts.count { it.isCorrect }
        val accuracyRate = correctCount.toDouble() / recentAttempts.size

        when {
            accuracyRate >= 0.8 && gameSession.currentDifficulty == QuizDifficulty.EASY -> {
                gameSession.currentDifficulty = QuizDifficulty.MEDIUM
            }
            accuracyRate >= 0.8 && gameSession.currentDifficulty == QuizDifficulty.MEDIUM -> {
                gameSession.currentDifficulty = QuizDifficulty.HARD
            }
            accuracyRate < 0.4 && gameSession.currentDifficulty == QuizDifficulty.HARD -> {
                gameSession.currentDifficulty = QuizDifficulty.MEDIUM
            }
            accuracyRate < 0.4 && gameSession.currentDifficulty == QuizDifficulty.MEDIUM -> {
                gameSession.currentDifficulty = QuizDifficulty.EASY
            }
        }
    }
}
