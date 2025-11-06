package team.exit_1.repo.backend.core.service.domain.game.data.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import team.exit_1.repo.backend.core.service.domain.game.data.constant.QuizDifficulty

@Schema(description = "퀴즈 상세 정보 (시도 내)")
data class QuizDetailInAttempt(
    @field:Schema(description = "퀴즈 ID", example = "1")
    @field:JsonProperty("quiz_id")
    val quizId: Long,

    @field:Schema(description = "질문 타입", example = "fill_in_blank")
    @field:JsonProperty("question_type")
    val questionType: QuestionTypeDto?,

    @field:Schema(description = "질문", example = "당신의 고향은 서울인가요?")
    @field:JsonProperty("question")
    val question: String,

    @field:Schema(description = "선택지 (객관식인 경우)")
    @field:JsonProperty("options")
    val options: List<QuizOption>?,

    @field:Schema(description = "난이도", example = "EASY")
    @field:JsonProperty("difficulty")
    val difficulty: QuizDifficulty,

    @field:Schema(description = "주제", example = "자전적 기억")
    @field:JsonProperty("topic")
    val topic: String?,

    @field:Schema(description = "기반 대화", example = "지난번에 고향에 대해 이야기했었습니다")
    @field:JsonProperty("based_on_conversation")
    val basedOnConversation: String?,

    @field:Schema(description = "카테고리", example = "자전적 기억")
    @field:JsonProperty("category")
    val category: String?,

    @field:Schema(description = "힌트", example = "태어난 곳을 떠올려보세요")
    @field:JsonProperty("hint")
    val hint: String?
)
