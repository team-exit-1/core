package team.exit_1.repo.backend.core.service.global.config

import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationPropertiesScan(
    basePackages = [
        "team.exit_1.repo.backend.core.service.global.security.data",
    ],
)
class PropertiesScanConfig
