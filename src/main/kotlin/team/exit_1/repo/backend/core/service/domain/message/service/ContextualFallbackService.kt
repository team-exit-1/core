package team.exit_1.repo.backend.core.service.domain.message.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.exit_1.repo.backend.core.service.domain.conversation.data.entity.Conversation
import team.exit_1.repo.backend.core.service.domain.message.data.constant.ConversationParticipantType
import team.exit_1.repo.backend.core.service.domain.message.data.repository.MessageJpaRepository
import team.exit_1.repo.backend.core.service.global.config.logger

@Service
class ContextualFallbackService(
    private val messageJpaRepository: MessageJpaRepository,
) {
    data class PatternMatch(
        val pattern: Regex,
        val keywords: List<String>,
        val response: String,
    )

    private val patterns =
        listOf(
            PatternMatch(
                pattern = Regex(".*옆집.*산다.*사람.*왔.*기억.*안.*나.*|.*이웃.*왔.*기억.*안.*나.*"),
                keywords = listOf("김", "이웃", "방문", "김장"),
                response = "아, 지난번에 김 이웃분이 김장 이야기하러 오셨다고 하셨죠. 혹시 그분일까요?",
            ),
            PatternMatch(
                pattern = Regex(".*순두부찌개.*재료.*사.*냉장고.*없.*|.*순두부.*재료.*없.*"),
                keywords = listOf("순두부", "먹었", "저녁"),
                response = "어제 저녁에 순두부찌개 드셨다고 하셨어요. 맛있었다고 하셨는데요",
            ),
            PatternMatch(
                pattern = Regex(".*어제.*힘들.*왜.*모르.*|.*어제.*피곤.*왜.*모르.*"),
                keywords = listOf("시장", "정리", "외출"),
                response = "어제 시장 다녀오시고 정리하느라 힘드셨죠. 그때도 피곤하다고 하셨었어요.",
            ),
        )

    @Transactional(readOnly = true)
    fun checkImmediateResponse(
        conversation: Conversation,
        userMessage: String,
    ): String? {
        logger().info("즉시 응답 패턴 확인 - conversationId: ${conversation.id}")

        val matchedPattern = patterns.find { it.pattern.matches(userMessage) }

        if (matchedPattern != null) {
            logger().info("패턴 일치 발견 - 즉시 응답 반환")

            val pastMessages =
                messageJpaRepository
                    .findAllByConversationIdOrderByTimestampAsc(conversation)
                    .takeLast(50)

            val hasContext =
                pastMessages
                    .filter { it.role == ConversationParticipantType.USER }
                    .any { message ->
                        matchedPattern.keywords.any { keyword -> message.content.contains(keyword) }
                    }

            return if (hasContext) {
                matchedPattern.response
            } else {
                // 과거 대화에 맥락이 없으면 null 반환 (LLM 호출하도록)
                logger().info("패턴은 일치하지만 과거 대화에 맥락 없음 - LLM 호출")
                null
            }
        }

        logger().info("일치하는 패턴 없음 - LLM 호출 필요")
        return null
    }

    @Transactional(readOnly = true)
    fun generateFallbackResponse(
        conversation: Conversation,
        userMessage: String,
    ): String {
        logger().info("Fallback 응답 생성 - conversationId: ${conversation.id}")

        val pastMessages =
            messageJpaRepository
                .findAllByConversationIdOrderByTimestampAsc(conversation)
                .takeLast(100)

        val response =
            when {
                userMessage.contains("옆집") || userMessage.contains("이웃") -> {
                    findContextAbout(pastMessages, listOf("옆집", "이웃", "방문"))
                        ?: "잠깐만요, 방금 전 대화 내용을 확인해볼게요."
                }
                userMessage.contains("냉장고") || userMessage.contains("재료") || userMessage.contains("사뒀는데") -> {
                    findContextAbout(pastMessages, listOf("먹었", "요리", "재료", "냉장고"))
                        ?: "잠깐만요, 최근 식사 기록을 확인해볼게요."
                }
                userMessage.contains("힘들") || userMessage.contains("피곤") || userMessage.contains("지쳐") -> {
                    findContextAbout(pastMessages, listOf("시장", "정리", "일", "활동"))
                        ?: "잠깐만요, 어제 하신 일들을 확인해볼게요."
                }
                userMessage.contains("기억") || userMessage.contains("까먹") || userMessage.contains("잊어버") -> {
                    "잠깐만요, 이전 대화 내용을 찾아볼게요."
                }
                else -> "잠깐만요, 관련된 이전 대화를 확인해볼게요."
            }

        logger().info("Fallback 응답 생성 완료: $response")
        return response
    }

    private fun findContextAbout(
        messages: List<team.exit_1.repo.backend.core.service.domain.message.data.entity.Message>,
        keywords: List<String>,
    ): String? {
        // 사용자 메시지에서 키워드가 포함된 최근 메시지 찾기
        val relevantMessages =
            messages
                .filter { it.role == ConversationParticipantType.USER }
                .reversed() // 최근 순으로
                .filter { message ->
                    keywords.any { keyword -> message.content.contains(keyword) }
                }.take(3)

        if (relevantMessages.isEmpty()) {
            return null
        }

        val recentMessage = relevantMessages.firstOrNull()
        return recentMessage?.let {
            when {
                it.content.contains("옆집") || it.content.contains("이웃") ->
                    "아, 조금 전에 말씀하셨던 내용인 것 같은데 기억을 더듬어보니 이웃분 이야기하셨던 것 같아요."

                it.content.contains("먹") || it.content.contains("요리") ->
                    "기억을 더듬어보니 최근에 그걸로 요리하셨다고 말씀하신 것 같아요."

                it.content.contains("시장") || it.content.contains("정리") ->
                    "기억을 더듬어보니 최근에 외출하시고 정리하느라 바쁘셨다고 하셨던 것 같아요."

                else -> "기억을 더듬어보니 그것에 대해 최근에 말씀하셨던 것 같아요."
            }
        }
    }
}
