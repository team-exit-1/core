package team.exit_1.repo.backend.core.service.global.security.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import team.exit_1.repo.backend.core.service.global.security.data.CorsEnvironment

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(CorsEnvironment::class)
class SecurityConfig(
    private val corsEnvironment: CorsEnvironment,
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf(CsrfConfigurer<*>::disable)
            .cors {
                it.configurationSource {
                    val cors = CorsConfiguration()
                    cors.allowedOrigins = corsEnvironment.allowedOrigins
                    cors.addAllowedHeader("*")
                    cors.allowedMethods = HttpMethod.values().map(HttpMethod::name)
                    cors.allowCredentials = true
                    cors.maxAge = 3600L
                    cors
                }
            }.httpBasic(HttpBasicConfigurer<*>::disable)
            .formLogin(FormLoginConfigurer<*>::disable)
            .logout(LogoutConfigurer<*>::disable)
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .formLogin { it.disable() }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers(
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/actuator/**",
                    ).permitAll()
                    .anyRequest()
                    .permitAll()
            }

        return http.build()
    }

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()
}
