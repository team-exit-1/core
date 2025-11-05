package team.exit_1.repo.backend.core.service.global.common.error.handler

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.validation.ConstraintViolationException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.multipart.MaxUploadSizeExceededException
import org.springframework.http.converter.HttpMessageNotReadableException
import team.exit_1.repo.backend.core.service.global.common.error.exception.ExpectedException
import team.exit_1.repo.backend.core.service.global.common.response.data.reponse.CommonApiResponse

@RestControllerAdvice
class GlobalExceptionHandler {
    private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)
    private val objectMapper = ObjectMapper()

    @ExceptionHandler(ExpectedException::class)
    private fun expectedException(ex: ExpectedException): CommonApiResponse<Nothing> {
        logger.warn("예상된 예외 발생: {}", ex.message)
        logger.trace("예상된 예외 상세: ", ex)
        return CommonApiResponse.error(ex.message ?: "오류가 발생했습니다", ex.statusCode)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun validationException(ex: MethodArgumentNotValidException): CommonApiResponse<Nothing> {
        logger.warn("유효성 검증 실패: {}", ex.message)
        logger.trace("유효성 검증 실패 상세: ", ex)
        return CommonApiResponse.error(
            methodArgumentNotValidExceptionToJson(ex),
            HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun httpMessageNotReadableException(ex: HttpMessageNotReadableException): CommonApiResponse<Nothing> {
        logger.warn("잘못된 요청 본문: {}", ex.message)
        logger.trace("잘못된 요청 본문 상세: ", ex)
        return CommonApiResponse.error("요청 본문 형식이 올바르지 않습니다", HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun validationException(ex: ConstraintViolationException): CommonApiResponse<Nothing> {
        logger.warn("필드 유효성 검증 실패: {}", ex.message)
        logger.trace("필드 유효성 검증 실패 상세: ", ex)
        return CommonApiResponse.error("필드 유효성 검증 실패: ${ex.message}", HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(RuntimeException::class)
    fun unExpectedException(ex: RuntimeException): CommonApiResponse<Nothing> {
        logger.error("예상치 못한 예외 발생: ", ex)
        return CommonApiResponse.error("내부 서버 오류가 발생했습니다", HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(MaxUploadSizeExceededException::class)
    fun maxUploadSizeExceededException(ex: MaxUploadSizeExceededException): CommonApiResponse<Nothing> {
        logger.warn("파일 크기 초과: {}", ex.message)
        logger.trace("파일 크기 초과 상세: ", ex)
        return CommonApiResponse.error(
            "파일 크기가 너무 큽니다. 제한 크기: ${ex.maxUploadSize}",
            HttpStatus.BAD_REQUEST
        )
    }

    private fun methodArgumentNotValidExceptionToJson(ex: MethodArgumentNotValidException): String {
        val globalResults = mutableMapOf<String, Any>()
        val fieldResults = mutableMapOf<String, String?>()
        ex.bindingResult.globalErrors.forEach { error ->
            globalResults[ex.bindingResult.objectName] = error.defaultMessage ?: ""
        }
        ex.bindingResult.fieldErrors.forEach { error ->
            fieldResults[error.field] = error.defaultMessage
        }
        globalResults[ex.bindingResult.objectName] = fieldResults
        return objectMapper.writeValueAsString(globalResults).replace("\"", "'")
    }
}