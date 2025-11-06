package team.exit_1.repo.backend.core.service.domain.routine.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.exit_1.repo.backend.core.service.domain.routine.data.dto.response.RoutineListResponse
import team.exit_1.repo.backend.core.service.domain.routine.data.dto.response.RoutineResponse
import team.exit_1.repo.backend.core.service.domain.routine.data.repository.RoutineJpaRepository
import team.exit_1.repo.backend.core.service.global.config.MockDataConfig

@Service
class QueryRoutinesService(
    private val routineJpaRepository: RoutineJpaRepository
) {
    @Transactional(readOnly = true)
    fun execute(): RoutineListResponse {
        val userId = MockDataConfig.MOCK_USER_ID
        val routines = routineJpaRepository.findAllByUserId(userId)

        val items = routines.map { routine ->
            RoutineResponse(
                id = routine.id!!,
                userId = routine.userId!!,
                title = routine.title!!,
                content = routine.content!!,
                times = routine.times!!,
                createdAt = routine.createdAt!!,
                updatedAt = routine.updatedAt!!
            )
        }

        return RoutineListResponse(
            items = items,
            total = items.size
        )
    }
}