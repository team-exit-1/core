package team.exit_1.repo.backend.core.service.domain.game.data.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import team.exit_1.repo.backend.core.service.domain.game.data.constant.GameSessionStatus
import team.exit_1.repo.backend.core.service.domain.game.data.constant.QuizDifficulty
import java.time.LocalDateTime

@Table(name = "tb_game_sessions")
@Entity
@DynamicInsert
@DynamicUpdate
class GameSession {
    @Id
    @Column(name = "session_id")
    var id: String? = null

    @Column(name = "user_id", nullable = false)
    var userId: String? = null

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: GameSessionStatus = GameSessionStatus.IN_PROGRESS

    @Column(name = "start_time", nullable = false)
    var startTime: LocalDateTime? = null

    @Column(name = "end_time")
    var endTime: LocalDateTime? = null

    @Column(name = "total_score", nullable = false)
    var totalScore: Int = 0

    @Column(name = "current_difficulty", nullable = false)
    @Enumerated(EnumType.STRING)
    var currentDifficulty: QuizDifficulty = QuizDifficulty.EASY
}
