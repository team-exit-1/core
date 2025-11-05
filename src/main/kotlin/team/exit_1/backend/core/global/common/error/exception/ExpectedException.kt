package team.exit_1.backend.core.global.common.error.exception

import org.springframework.http.HttpStatus

class ExpectedException(
    message: String,
    val statusCode: HttpStatus,
) : RuntimeException(message) {
    constructor(statusCode: HttpStatus) : this(statusCode.reasonPhrase, statusCode)

    override fun fillInStackTrace(): Throwable = this
}