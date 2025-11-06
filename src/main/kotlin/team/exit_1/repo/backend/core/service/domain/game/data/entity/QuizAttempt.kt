package team.exit_1.repo.backend.core.service.domain.game.data.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import java.time.LocalDateTime

@Table(name = "tb_quiz_attempts")
@Entity
@DynamicInsert
@DynamicUpdate
class QuizAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attempt_id")
    var id: Long? = null

    @JoinColumn(name = "session_id", nullable = false)
    @ManyToOne(targetEntity = GameSession::class)
    var gameSession: GameSession? = null

    @JoinColumn(name = "quiz_id", nullable = false)
    @ManyToOne(targetEntity = Quiz::class)
    var quiz: Quiz? = null

    @Column(name = "user_answer", nullable = false)
    var userAnswer: String? = null

    @Column(name = "is_correct", nullable = false)
    var isCorrect: Boolean = false

    @Column(name = "score", nullable = false)
    var score: Int = 0

    @Column(name = "attempt_time", nullable = false)
    var attemptTime: LocalDateTime? = null
}
