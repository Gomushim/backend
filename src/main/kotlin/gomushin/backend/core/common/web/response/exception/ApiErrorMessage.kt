package gomushin.backend.core.common.web.response.exception

import gomushin.backend.core.common.web.response.MessageResolver

data class ApiErrorMessage(
    val code: String,
    val control: Control = Control.EXPOSE_IF_RESOLVED,
    val resolved: String? = MessageResolver.resolve(code),
    val args: Array<out Any>? = null,
) {
    enum class Control {
        EXPOSE_IF_RESOLVED, HIDE
    }

    companion object {
        fun of(code: String, control: Control = Control.EXPOSE_IF_RESOLVED): ApiErrorMessage =
            ApiErrorMessage(code, control, MessageResolver.resolve(code))

        fun of(code: String, control: Control = Control.EXPOSE_IF_RESOLVED, args: Array<out Any>?): ApiErrorMessage =
            ApiErrorMessage(code, control, MessageResolver.resolve(code, args), args)
    }
}
