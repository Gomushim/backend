package gomushin.backend.core.common.web.response.exception

data class ApiError(val element: ApiErrorElement) {
    companion object {
        fun of(element: ApiErrorElement): ApiError = ApiError(element)
    }
}
