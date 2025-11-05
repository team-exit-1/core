package team.exit_1.backend.core.global.security.config

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
import org.springframework.web.cors.CorsConfigurationSource
import team.exit_1.backend.core.global.security.data.CorsEnvironment

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val corsEnvironment: CorsEnvironment,
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf(CsrfConfigurer<*>::disable).cors {
                it.configurationSource(CorsConfigurationSource() {
                    val cors = CorsConfiguration()
                    cors.allowedOrigins = corsEnvironment.allowedOrigins
                    cors.addAllowedHeader("*")
                    cors.allowedMethods = HttpMethod.values().map(HttpMethod::name)
                    cors.allowCredentials = true
                    cors.maxAge = 3600L
                    cors
                })
            }.httpBasic(HttpBasicConfigurer<*>::disable).formLogin(FormLoginConfigurer<*>::disable)
            .logout(LogoutConfigurer<*>::disable)
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { auth ->
                auth.requestMatchers(
                        "/actuator/**",
                    ).permitAll().anyRequest().authenticated()
            }

        return http.build()
    }


    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()
}