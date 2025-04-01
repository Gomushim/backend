package gomushin.backend.core.common.web.response.exception

import gomushin.backend.core.common.support.SpringContextHolder
import gomushin.backend.core.common.web.response.ExtendedHttpStatus
import gomushin.backend.core.configuration.env.AppEnv

open class ErrorCodeResolvingApiErrorException : ApiErrorException {

    private companion object {
        val appEnv: AppEnv by lazy {
            SpringContextHolder.getBean(AppEnv::class.java)
        }
    }

    constructor(statusCode: ExtendedHttpStatus, code: String) :
            super(
                ApiError.of(
                    ApiErrorElement(
                        appEnv.getId(), statusCode, ApiErrorCode.of(code), ApiErrorMessage.of(code)
                    )
                )
            )

    constructor(statusCode: ExtendedHttpStatus, code: String, cause: Throwable?) :
            super(
                ApiError.of(
                    ApiErrorElement(
                        appEnv.getId(), statusCode, ApiErrorCode.of(code), ApiErrorMessage.of(code)
                    )
                ), cause
            )

    constructor(statusCode: ExtendedHttpStatus, code: String, args: Array<out Any>?) :
            super(
                ApiError.of(
                    ApiErrorElement(
                        appEnv.getId(), statusCode, ApiErrorCode.of(code), ApiErrorMessage.of(code, args = args)
                    )
                )
            )

    constructor(statusCode: ExtendedHttpStatus, code: String, args: Array<out Any>?, cause: Throwable?) :
            super(
                ApiError.of(
                    ApiErrorElement(
                        appEnv.getId(), statusCode, ApiErrorCode.of(code), ApiErrorMessage.of(code, args = args)
                    )
                ), cause
            )

    constructor(statusCode: ExtendedHttpStatus, code: String, args: Array<out Any>?, cause: Throwable?, data: Any?) :
            super(
                ApiError.of(
                    ApiErrorElement(
                        appEnv.getId(), statusCode, ApiErrorCode.of(code), ApiErrorMessage.of(code, args = args), data
                    )
                ), cause
            )
}
