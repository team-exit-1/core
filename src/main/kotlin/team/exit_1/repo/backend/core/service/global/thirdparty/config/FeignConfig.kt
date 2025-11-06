package team.exit_1.repo.backend.core.service.global.thirdparty.config

import com.fasterxml.jackson.databind.ObjectMapper
import feign.Contract
import feign.codec.Decoder
import feign.codec.Encoder
import feign.jackson.JacksonDecoder
import feign.jackson.JacksonEncoder
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.cloud.openfeign.support.SpringMvcContract
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import team.exit_1.repo.backend.core.service.global.thirdparty.error.FeignErrorDecoder

@Configuration
@EnableFeignClients(basePackages = ["team.exit_1"])
class FeignConfig(
    private val objectMapper: ObjectMapper
) {
    @Bean
    fun feignErrorDecoder(): FeignErrorDecoder = FeignErrorDecoder()

    @Bean
    fun encoder(): Encoder = JacksonEncoder(objectMapper)

    @Bean
    fun decoder(): Decoder = JacksonDecoder(objectMapper)

    @Bean
    fun contract(): Contract = SpringMvcContract()
}