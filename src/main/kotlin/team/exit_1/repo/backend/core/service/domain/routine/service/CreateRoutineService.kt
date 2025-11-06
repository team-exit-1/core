package team.exit_1.repo.backend.core.service.domain.routine.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.exit_1.repo.backend.core.service.domain.routine.data.dto.request.CreateRoutineRequest
import team.exit_1.repo.backend.core.service.domain.routine.data.dto.response.RoutineResponse
import team.exit_1.repo.backend.core.service.domain.routine.data.entity.Routine
import team.exit_1.repo.backend.core.service.domain.routine.data.repository.RoutineJpaRepository
import team.exit_1.repo.backend.core.service.global.config.MockDataConfig
import java.time.LocalDateTime

@Service
class CreateRoutineService(
    private val routineJpaRepository: RoutineJpaRepository
) {
    @Transactional
    fun execute(request: CreateRoutineRequest): RoutineResponse {
        val userId = MockDataConfig.MOCK_USER_ID
        val now = LocalDateTime.now()

        val routine = Routine().apply {
            this.userId = userId
            this.title = request.title
            this.content = request.content
            this.times = request.times
            this.createdAt = now
            this.updatedAt = now
        }

        val savedRoutine = routineJpaRepository.save(routine)

        return RoutineResponse(
            id = savedRoutine.id!!,
            userId = savedRoutine.userId!!,
            title = savedRoutine.title!!,
            content = savedRoutine.content!!,
            times = savedRoutine.times!!,
            createdAt = savedRoutine.createdAt!!,
            updatedAt = savedRoutine.updatedAt!!
        )
    }
}