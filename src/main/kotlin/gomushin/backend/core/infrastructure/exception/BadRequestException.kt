package gomushin.backend.core.infrastructure.exception

import gomushin.backend.core.common.web.response.ExtendedHttpStatus
import gomushin.backend.core.common.web.response.exception.ErrorCodeResolvingApiErrorException

class BadRequestException(code: String = "bad-request", cause: Throwable? = null) :
    ErrorCodeResolvingApiErrorException(ExtendedHttpStatus.BAD_REQUEST, code, cause)
