package gomushin.backend.schedule.dto.request

import io.swagger.v3.oas.annotations.media.Schema

data class UpsertLetterRequest(
    @Schema(description = "편지 ID(새로 생성 시 null, 업데이트 시 id)", example = "1")
    val letterId: Long? = null,
    @Schema(description = "일정 ID", example = "1")
    val scheduleId: Long,
    @Schema(description = "편지 제목", example = "훈련 힘내")
    val title: String,
    @Schema(description = "편지 내용", example = "화이팅")
    val content: String,
    @Schema(description = "편지 사진 URL 목록", example = "[\"https://example.com/picture1.jpg\", \"https://example.com/picture2.jpg\"]")
    val pictureUrls : List<String> = emptyList(),
)
