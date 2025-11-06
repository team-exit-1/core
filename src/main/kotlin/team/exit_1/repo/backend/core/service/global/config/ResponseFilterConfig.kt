package team.exit_1.repo.backend.core.service.global.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "response.filter")
data class ResponseFilterConfig(
    var enabled: Boolean = true,
    var patterns: List<String> =
        listOf(
            "^안녕하세요[!.?]*\\s*",
            "^물론입니다[!.?]*\\s*",
            "^네[!.?]*\\s*",
            "^좋습니다[!.?]*\\s*",
            "^알겠습니다[!.?]*\\s*",
            "^그렇습니다[!.?]*\\s*",
            "^물론이죠[!.?]*\\s*",
            "^네네[!.?]*\\s*",
            "^좋아요[!.?]*\\s*",
            "^괜찮습니다[!.?]*\\s*",
        ),
)