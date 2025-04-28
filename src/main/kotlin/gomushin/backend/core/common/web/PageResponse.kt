package gomushin.backend.core.common.web

import io.swagger.v3.oas.annotations.media.Schema

data class PageResponse<T>(
    @Schema(description = "페이지 응답")
    val data: List<T>?,
    @Schema(description = "다음 페이지를 위한 커서 키")
    val after: Long?,
    @Schema(description = "데이터 수")
    val count: Int,
    @Schema(description = "다음 페이지 URL")
    val next: String?,
    @Schema(description = "마지막 페이지 여부")
    val isLastPage: Boolean,
) {
    companion object {
        fun <T> of(
            data: List<T>,
            after: Long?,
            count: Int,
            next: String?,
            isLastPage: Boolean,
        ): PageResponse<T> {
            return PageResponse(
                data = data,
                after = after,
                count = count,
                next = next,
                isLastPage = isLastPage
            )
        }
    }
}
