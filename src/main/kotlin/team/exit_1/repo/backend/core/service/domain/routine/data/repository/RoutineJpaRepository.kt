package team.exit_1.repo.backend.core.service.domain.routine.data.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import team.exit_1.repo.backend.core.service.domain.routine.data.entity.Routine

@Repository
interface RoutineJpaRepository : JpaRepository<Routine, Long> {
    fun findAllByUserId(userId: String): List<Routine>
}