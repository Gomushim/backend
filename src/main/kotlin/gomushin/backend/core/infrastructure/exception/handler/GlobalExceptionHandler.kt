package gomushin.backend.core.infrastructure.exception.handler

import gomushin.backend.core.common.support.SpringContextHolder
import gomushin.backend.core.common.web.response.ApiResponse
import gomushin.backend.core.common.web.response.ExtendedHttpStatus
import gomushin.backend.core.common.web.response.exception.ApiError
import gomushin.backend.core.common.web.response.exception.ApiErrorCode
import gomushin.backend.core.common.web.response.exception.ApiErrorElement
import gomushin.backend.core.common.web.response.exception.ApiErrorMessage
import gomushin.backend.core.configuration.env.AppEnv
import gomushin.backend.core.infrastructure.exception.BadRequestException
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleMethodArgumentNotValidException(ex: MethodArgumentNotValidException): ApiResponse<Nothing> {

        val code = ex.bindingResult
            .fieldErrors
            .firstOrNull()
            ?.defaultMessage
            ?: "bad-request"
        return ApiResponse.error(
            ApiError.of(
                ApiErrorElement(
                    SpringContextHolder.getBean(AppEnv::class.java).getId(),
                    ExtendedHttpStatus.BAD_REQUEST,
                    ApiErrorCode.of(code),
                    ApiErrorMessage.of(code)
                )
            )
        )
    }
}