package team.exit_1.repo.backend.core.service.global.config

// 스태틱한 상수를 모아둠
class MockDataConfig {
    companion object {
        const val MOCK_USER_ID = "user_2419"
        const val MOCK_CONVERSATION_ID = "conv_001"
        const val GAME_SESSION_TIME_LIMIT_HOURS = 24L // 게임 세션 시간 제한 (시간)
        const val MAX_QUIZ_COUNT_PER_SESSION = 6L // 세션당 최대 퀴즈 개수
    }
}
