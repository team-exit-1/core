package team.exit_1.repo.backend.core.service.global.common.error.exception

import org.springframework.http.HttpStatus

class FeignClientException(
    message: String,
    val status: HttpStatus,
) : RuntimeException(message)
