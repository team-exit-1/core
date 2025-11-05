package team.exit_1.backend.core.global.common.error.exception

import org.springframework.http.HttpStatus

class FeignClientException(
    message: String,
    val status: HttpStatus,
) : RuntimeException(message)