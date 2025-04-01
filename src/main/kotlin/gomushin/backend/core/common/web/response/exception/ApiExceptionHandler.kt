package gomushin.backend.core.common.web.response.exception

import gomushin.backend.core.common.web.response.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ApiExceptionHandler {

    @ExceptionHandler(ApiErrorException::class)
    fun handleApiErrorExtention(ex: ApiErrorException): ResponseEntity<ApiResponse<Nothing>> {
        val status = ex.error.element.status
        return ResponseEntity.status(status.code).body(ApiResponse.error(ex.error))
    }
}
