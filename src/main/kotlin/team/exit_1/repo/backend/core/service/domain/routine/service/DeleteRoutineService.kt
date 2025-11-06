package team.exit_1.repo.backend.core.service.domain.routine.service

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.exit_1.repo.backend.core.service.domain.routine.data.repository.RoutineJpaRepository
import team.exit_1.repo.backend.core.service.global.common.error.exception.ExpectedException

@Service
class DeleteRoutineService(
    private val routineJpaRepository: RoutineJpaRepository
) {
    @Transactional
    fun execute(routineId: Long) {
        val routine = routineJpaRepository.findById(routineId)
            .orElseThrow {
                ExpectedException(
                    message = "루틴을 찾을 수 없습니다.",
                    statusCode = HttpStatus.NOT_FOUND
                )
            }

        routineJpaRepository.delete(routine)
    }
}