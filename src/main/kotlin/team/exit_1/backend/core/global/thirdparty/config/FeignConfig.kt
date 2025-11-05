package team.exit_1.backend.core.global.thirdparty.config

import feign.Contract
import feign.codec.Decoder
import feign.codec.Encoder
import feign.jackson.JacksonDecoder
import feign.jackson.JacksonEncoder
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.cloud.openfeign.support.SpringMvcContract
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import team.exit_1.backend.core.global.thirdparty.error.FeignErrorDecoder

@Configuration
@EnableFeignClients(basePackages = ["team.exit_1"])
class FeignConfig {
    @Bean
    fun feignErrorDecoder(): FeignErrorDecoder = FeignErrorDecoder()

    @Bean
    fun encoder(): Encoder = JacksonEncoder()

    @Bean
    fun decoder(): Decoder = JacksonDecoder()

    @Bean
    fun contract(): Contract = SpringMvcContract()
}