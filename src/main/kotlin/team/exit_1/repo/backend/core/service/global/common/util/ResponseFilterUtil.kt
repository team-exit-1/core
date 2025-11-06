package team.exit_1.repo.backend.core.service.global.common.util

import org.springframework.stereotype.Component
import team.exit_1.repo.backend.core.service.global.config.ResponseFilterConfig

@Component
class ResponseFilterUtil(
    private val responseFilterConfig: ResponseFilterConfig,
) {
    fun filterResponse(response: String): String {
        if (!responseFilterConfig.enabled) {
            return response
        }

        var filteredResponse = response.trim()

        // 각 패턴을 순차적으로 적용하여 문장 시작 부분의 불필요한 인삿말 제거
        responseFilterConfig.patterns.forEach { pattern ->
            filteredResponse = filteredResponse.replace(Regex(pattern), "")
        }

        // 여러 번 적용하여 중첩된 인삿말도 제거 (최대 3번)
        repeat(2) {
            val before = filteredResponse
            responseFilterConfig.patterns.forEach { pattern ->
                filteredResponse = filteredResponse.replace(Regex(pattern), "")
            }
            if (before == filteredResponse) return@repeat // 변화가 없으면 중단
        }

        return filteredResponse.trim()
    }
}