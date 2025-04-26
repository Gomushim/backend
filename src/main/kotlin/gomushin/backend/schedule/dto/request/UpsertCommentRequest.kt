package gomushin.backend.schedule.dto.request

import io.swagger.v3.oas.annotations.media.Schema

data class UpsertCommentRequest(
    @Schema(description = "댓글 ID(새로 생성 시 null, 업데이트 시 id)", example = "1")
    val commentId: Long?,
    @Schema(description = "내용", example = "훈련 힘내")
    val content: String,
)
