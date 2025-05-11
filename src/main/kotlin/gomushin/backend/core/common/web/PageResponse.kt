package gomushin.backend.core.common.web

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.data.domain.Page

data class PageResponse<T>(
    @Schema(description = "페이지 응답")
    val data: List<T>,
    @Schema(description = "전체 페이지 수")
    val totalPages: Int,
    @Schema(description = "마지막 페이지 여부")
    val isLastPage: Boolean,
) {
    companion object {
        fun <T> from(
            page: Page<T>
        ): PageResponse<T> = PageResponse(
            data = page.content,
            totalPages = page.totalPages,
            isLastPage = page.isLast
        )
    }
}
