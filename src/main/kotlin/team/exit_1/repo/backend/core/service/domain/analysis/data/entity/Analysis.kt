package team.exit_1.repo.backend.core.service.domain.analysis.data.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import java.time.LocalDateTime

@Table(name = "tb_analysis")
@Entity
@DynamicInsert
@DynamicUpdate
class Analysis {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "analysis_id", nullable = false)
    var id: Long? = null

    @Column(name = "user_id", nullable = false, length = 100)
    var userId: String? = null

    @Column(name = "domains", nullable = false, columnDefinition = "TEXT")
    var domains: String? = null

    @Column(name = "report", nullable = false, columnDefinition = "TEXT")
    var report: String? = null

    @Column(name = "analyzed_at", nullable = false)
    var analyzedAt: LocalDateTime? = null

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime? = null
}