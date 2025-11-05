package team.exit_1.repo.backend.core.service.domain.data.entity

import jakarta.persistence.*
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import team.exit_1.repo.backend.core.service.domain.data.constant.GameSessionStatus
import java.time.LocalDateTime

@Table(name = "tb_game_sessions")
@Entity
@DynamicInsert
@DynamicUpdate
class GameSession {
    @Id
    @Column(name = "session_id")
    var id: String? = null

    @JoinColumn(name = "conversation_id", nullable = false)
    @ManyToOne(targetEntity = Conversation::class)
    var conversation: Conversation? = null

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
    var currentDifficulty: team.exit_1.repo.backend.core.service.domain.data.constant.QuizDifficulty =
        team.exit_1.repo.backend.core.service.domain.data.constant.QuizDifficulty.EASY
}
