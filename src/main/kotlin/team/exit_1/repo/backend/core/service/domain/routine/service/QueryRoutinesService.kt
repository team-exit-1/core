package team.exit_1.repo.backend.core.service.domain.routine.service

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.exit_1.repo.backend.core.service.domain.routine.data.dto.response.RoutineListResponse
import team.exit_1.repo.backend.core.service.domain.routine.data.dto.response.RoutineResponse
import team.exit_1.repo.backend.core.service.domain.routine.data.repository.RoutineJpaRepository
import team.exit_1.repo.backend.core.service.global.config.MockDataConfig

@Service
class QueryRoutinesService(
    private val routineJpaRepository: RoutineJpaRepository,
    private val objectMapper: ObjectMapper
) {
    @Transactional(readOnly = true)
    fun execute(): RoutineListResponse {
        val userId = MockDataConfig.MOCK_USER_ID
        val routines = routineJpaRepository.findAllByUserId(userId)

        val items = routines.map { routine ->
            val dayOfWeekList = objectMapper.readValue(
                routine.dayOfWeek!!,
                object : TypeReference<List<String>>() {}
            )

            RoutineResponse(
                id = routine.id!!,
                userId = routine.userId!!,
                title = routine.title!!,
                content = routine.content!!,
                times = routine.times!!,
                dayOfWeek = dayOfWeekList,
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