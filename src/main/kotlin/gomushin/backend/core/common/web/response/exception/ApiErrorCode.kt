package gomushin.backend.core.common.web.response.exception

data class ApiErrorCode(val value: String) {
    companion object {
        fun of(code: String) = ApiErrorCode(code)
    }
}
