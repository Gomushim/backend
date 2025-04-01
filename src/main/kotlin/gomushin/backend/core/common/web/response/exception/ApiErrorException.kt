package gomushin.backend.core.common.web.response.exception

open class ApiErrorException(
    val error: ApiError,
    cause: Throwable? = null,
) : RuntimeException(cause)
