package gomushin.backend.core.common.web.response.exception

import gomushin.backend.core.common.web.response.ApiResponse
import gomushin.backend.core.infrastructure.filter.logging.LoggingFilter
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ApiExceptionHandler {
    private val log = LoggerFactory.getLogger(ApiExceptionHandler::class.java)
    @ExceptionHandler(ApiErrorException::class)
    fun handleApiErrorExtention(ex: ApiErrorException): ResponseEntity<ApiResponse<Nothing>> {
        val status = ex.error.element.status
        log.warn("[Error] errorStatus : {}, errorMessage : {}",ex.error.element.code.value, ex.error.element.message.resolved)
        return ResponseEntity.status(status.code).body(ApiResponse.error(ex.error))
    }
}
