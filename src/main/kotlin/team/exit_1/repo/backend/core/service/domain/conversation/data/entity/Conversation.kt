package team.exit_1.repo.backend.core.service.domain.conversation.data.entity

import jakarta.persistence.*
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import team.exit_1.repo.backend.core.service.domain.conversation.data.constant.ConversationStatus
import java.time.LocalDateTime

@Table(name = "tb_conversations")
@Entity
@DynamicInsert
@DynamicUpdate
class Conversation {
    @Id
    @Column(name = "conversation_id", nullable = false)
    var id: String? = null

    @Column(name = "user_id", nullable = false)
    var userId: String? = null

    @Column
    var timestamp: LocalDateTime? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    var status: ConversationStatus = ConversationStatus.ACTIVE
}