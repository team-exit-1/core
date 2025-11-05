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
                            "CommonApiResponse" to createCommonApiResponseSchema()
                        )
                    )
                    .responses(
                        mutableMapOf(
                            "BadRequest" to createErrorApiResponse(400, "BAD_REQUEST", "잘못된 요청입니다"),
                            "Unauthorized" to createErrorApiResponse(401, "UNAUTHORIZED", "인증이 필요합니다"),
                            "Forbidden" to createErrorApiResponse(403, "FORBIDDEN", "접근 권한이 없습니다"),
                            "NotFound" to createErrorApiResponse(404, "NOT_FOUND", "리소스를 찾을 수 없습니다"),
                            "InternalServerError" to createErrorApiResponse(500, "INTERNAL_SERVER_ERROR", "내부 서버 오류가 발생했습니다")
                        )
                    )
            )

    private fun createCommonApiResponseSchema(): Schema<*> =
        Schema<Any>().apply {
            type = "object"
            addProperty("status", Schema<String>().type("string").example("OK"))
            addProperty("code", Schema<Int>().type("integer").example(200))
            addProperty("message", Schema<String>().type("string").example("성공했습니다"))
            addProperty("data", Schema<Any>().type("object").nullable(true))
        }

    private fun createErrorApiResponse(
        statusCode: Int,
        statusName: String,
        message: String,
    ): ApiResponse =
        ApiResponse()
            .description("Error Response")
            .content(
                Content()
                    .addMediaType(
                        "application/json",
                        MediaType()
                            .schema(Schema<Any>().`$ref`("#/components/schemas/CommonApiResponse"))
                            .example(
                                mapOf(
                                    "status" to statusName,
                                    "code" to statusCode,
                                    "message" to message,
                                    "data" to null
                                )
                            )
                    )
            )
}