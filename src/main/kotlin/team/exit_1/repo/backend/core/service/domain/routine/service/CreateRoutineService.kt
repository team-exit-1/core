package team.exit_1.repo.backend.core.service.domain.routine.service

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
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
    private val routineJpaRepository: RoutineJpaRepository,
    private val objectMapper: ObjectMapper
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
            this.dayOfWeek = objectMapper.writeValueAsString(request.dayOfWeek)
            this.createdAt = now
            this.updatedAt = now
        }

        val savedRoutine = routineJpaRepository.save(routine)

        val dayOfWeekList = objectMapper.readValue(
            savedRoutine.dayOfWeek!!,
            object : TypeReference<List<String>>() {}
        )

        return RoutineResponse(
            id = savedRoutine.id!!,
            userId = savedRoutine.userId!!,
            title = savedRoutine.title!!,
            content = savedRoutine.content!!,
            times = savedRoutine.times!!,
            dayOfWeek = dayOfWeekList,
            createdAt = savedRoutine.createdAt!!,
            updatedAt = savedRoutine.updatedAt!!
        )
    }
}