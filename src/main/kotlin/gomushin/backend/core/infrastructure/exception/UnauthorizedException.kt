package gomushin.backend.core.infrastructure.exception

import gomushin.backend.core.common.web.response.ExtendedHttpStatus
import gomushin.backend.core.common.web.response.exception.ErrorCodeResolvingApiErrorException

class UnauthorizedException(code: String = "unauthorized", cause: Throwable?) :
    ErrorCodeResolvingApiErrorException(ExtendedHttpStatus.UNAUTHORIZED, code, cause)
