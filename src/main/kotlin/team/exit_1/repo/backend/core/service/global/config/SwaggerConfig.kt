package team.exit_1.repo.backend.core.service.global.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.media.Content
import io.swagger.v3.oas.models.media.MediaType
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.oas.models.responses.ApiResponse
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@OpenAPIDefinition(
    info =
        Info(
            title = "Core Service API",
            description = "Exit-1 Backend Core Microservice API",
            version = "v1.0.0",
        ),
)
@Configuration
class SwaggerConfig {
    @Bean
    fun api(): GroupedOpenApi =
        GroupedOpenApi
            .builder()
            .group("Core Service API")
            .pathsToMatch("/**")
            .pathsToExclude("/actuator/**")
            .build()

    @Bean
    fun openAPI(): OpenAPI =
        OpenAPI()
            .components(
                Components()
                    .schemas(
                        mutableMapOf(
                            "ErrorResponse" to createErrorResponseSchema()
                        )
                    )
                    .responses(
                        mutableMapOf(
                            "BadRequest" to createErrorApiResponse(400, "잘못된 요청입니다"),
                            "Unauthorized" to createErrorApiResponse(401, "인증이 필요합니다"),
                            "Forbidden" to createErrorApiResponse(403, "접근 권한이 없습니다"),
                            "NotFound" to createErrorApiResponse(404, "리소스를 찾을 수 없습니다"),
                            "InternalServerError" to createErrorApiResponse(500, "내부 서버 오류가 발생했습니다")
                        )
                    )
            )

    private fun createErrorResponseSchema(): Schema<*> =
        Schema<Any>().apply {
            type = "object"
            addProperty("status", Schema<Int>().type("integer").example(400))
            addProperty("message", Schema<String>().type("string").example("오류 메시지"))
        }

    private fun createErrorApiResponse(
        statusCode: Int,
        message: String,
    ): ApiResponse =
        ApiResponse()
            .description("Error Response")
            .content(
                Content()
                    .addMediaType(
                        "application/json",
                        MediaType()
                            .schema(Schema<Any>().`$ref`("#/components/schemas/ErrorResponse"))
                            .example(
                                mapOf(
                                    "status" to statusCode,
                                    "message" to message
                                )
                            )
                    )
            )
}