package team.exit_1.repo.backend.core.service.domain.data.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import team.exit_1.repo.backend.core.service.domain.data.constant.ConversationParticipantType
import java.time.LocalDateTime

@Table(name = "tb_messages")
@Entity
@DynamicInsert
@DynamicUpdate
class Message {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id", nullable = false)
    var id: Long? = null

    @Column(name = "content", nullable = false, length = 1000)
    var content: String = ""

    @JoinColumn(name = "conversation_id", nullable = false)
    @ManyToOne(targetEntity = Conversation::class)
    @JsonIgnore
    var conversationId: Conversation? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    var role: ConversationParticipantType? = null

    @Column(nullable = false)
    var timestamp: LocalDateTime? = null
}