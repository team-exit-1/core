package team.exit_1.backend.core.global.config

import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Configuration

@Configuration
@EnableFeignClients(basePackages = ["team.exit_1"])
class FeignConfig {
}