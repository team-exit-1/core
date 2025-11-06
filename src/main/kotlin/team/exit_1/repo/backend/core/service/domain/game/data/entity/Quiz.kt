package team.exit_1.repo.backend.core.service.domain.game.data.entity

import jakarta.persistence.*
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import team.exit_1.repo.backend.core.service.domain.game.data.constant.QuestionType
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

    @Column(name = "llm_question_id", unique = true)
    var llmQuestionId: String? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "question_type")
    var questionType: QuestionType? = null

    @Column(nullable = false, columnDefinition = "TEXT")
    var question: String? = null

    @Column(name = "options", columnDefinition = "JSON")
    var options: String? = null // JSON string for multiple_choice

    @Column(name = "correct_answer", nullable = false)
    var correctAnswer: String? = null

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var difficulty: QuizDifficulty = QuizDifficulty.EASY

    @Column(name = "category")
    var category: String? = null

    @Column(name = "topic")
    var topic: String? = null

    @Column(name = "based_on_conversation", columnDefinition = "TEXT")
    var basedOnConversation: String? = null

    @Column(name = "memory_score")
    var memoryScore: Float? = null

    @Column(name = "days_since_conversation")
    var daysSinceConversation: Int? = null

    @Column(name = "hint", columnDefinition = "TEXT")
    var hint: String? = null
}
