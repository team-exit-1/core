package team.exit_1.repo.backend.core.service.domain.analysis.data.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import team.exit_1.repo.backend.core.service.domain.analysis.data.entity.Analysis
import java.time.LocalDateTime

@Repository
interface AnalysisJpaRepository : JpaRepository<Analysis, Long> {
    fun findAllByUserIdOrderByCreatedAtDesc(userId: String): List<Analysis>

    @Query("SELECT a FROM Analysis a WHERE a.userId = :userId AND a.createdAt >= :since ORDER BY a.createdAt DESC")
    fun findRecentByUserId(
        userId: String,
        since: LocalDateTime,
    ): List<Analysis>

    fun findFirstByUserIdOrderByCreatedAtDesc(userId: String): Analysis?
}
