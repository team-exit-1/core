package team.exit_1.repo.backend.core.service.domain.data.entity

import jakarta.persistence.*
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import java.time.LocalDateTime

@Table(name = "tb_conversations")
@Entity
@DynamicInsert
@DynamicUpdate
class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "conversation_id", nullable = false)
    var id: Long? = null

    @Column(name = "user_id", nullable = false)
    var userId: String? = null

    @Column
    var timestamp: LocalDateTime? = null
}