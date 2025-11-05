package team.exit_1.repo.backend.core.service.domain.game.data.entity

import jakarta.persistence.*
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import team.exit_1.repo.backend.core.service.domain.game.data.constant.QuizDifficulty

@Table(name = "tb_quizzes")
@Entity
@DynamicInsert
@DynamicUpdate
class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_id")
    var id: Long? = null

    @Column(nullable = false, columnDefinition = "TEXT")
    var question: String? = null

    @Column(name = "correct_answer", nullable = false)
    var correctAnswer: String? = null

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var difficulty: QuizDifficulty = QuizDifficulty.EASY

    @Column(name = "category")
    var category: String? = null

    @Column(name = "hint", columnDefinition = "TEXT")
    var hint: String? = null
}
