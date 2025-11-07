package team.exit_1.repo.backend.core.service.domain.analysis.data.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import team.exit_1.repo.backend.core.service.domain.analysis.data.entity.Analysis

@Repository
interface AnalysisJpaRepository : JpaRepository<Analysis, Long> {
    fun findAllByUserIdOrderByCreatedAtDesc(userId: String): List<Analysis>
}
