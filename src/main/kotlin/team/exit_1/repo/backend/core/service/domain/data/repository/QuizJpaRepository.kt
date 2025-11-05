package team.exit_1.repo.backend.core.service.domain.data.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import team.exit_1.repo.backend.core.service.domain.data.constant.QuizDifficulty
import team.exit_1.repo.backend.core.service.domain.data.entity.Quiz

@Repository
interface QuizJpaRepository : JpaRepository<Quiz, Long> {
    fun findAllByDifficulty(difficulty: QuizDifficulty): List<Quiz>
}
