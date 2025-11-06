package team.exit_1.repo.backend.core.service.domain.routine.data.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import java.time.LocalDateTime

@Table(name = "tb_routines")
@Entity
@DynamicInsert
@DynamicUpdate
class Routine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "routine_id")
    var id: Long? = null

    @Column(name = "user_id", nullable = false)
    var userId: String? = null

    @Column(name = "title", nullable = false)
    var title: String? = null

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    var content: String? = null

    @Column(name = "times", nullable = false)
    var times: String? = null

    @Column(name = "day_of_week", nullable = false, columnDefinition = "JSON")
    var dayOfWeek: String? = null

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime? = null

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime? = null
}
