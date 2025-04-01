package gomushin.backend.core.common.web.response.exception

import gomushin.backend.core.common.web.response.ExtendedHttpStatus

data class ApiErrorElement(
    val appId: String,
    val status: ExtendedHttpStatus,
    val code: ApiErrorCode,
    val message: ApiErrorMessage,
    val data: Any? = null,
)
