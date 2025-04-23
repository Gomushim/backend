package gomushin.backend.schedule.dto

import gomushin.backend.schedule.domain.entity.Schedule
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class UpsertScheduleRequest(
    @Schema(description = "일정 ID(새로 생성 시 null, 업데이트 시 id)", example = "1")
    val id: Long?,
    @Schema(description = "일정 내용", example = "훈련")
    val content: String,
    @Schema(description = "일정 시작 시간", example = "2023-10-01T10:00:00")
    val startDate: LocalDateTime,
    @Schema(description = "일정 종료 시간", example = "2023-10-01T12:00:00")
    val endDate: LocalDateTime,
    @Schema(description = "피로도 (VERY_TIRED, TIRED, GOOD) ", example = "VERT_TIRED")
    val fatigue: String,
    @Schema(description = "하루 종일 여부", example = "false")
    val isAllDay: Boolean = false,
) {
    fun toEntity(coupleId: Long, userId: Long) = Schedule(
        coupleId = coupleId,
        userId = userId,
        content = content,
        startDate = startDate,
        endDate = endDate,
        fatigue = fatigue,
        isAllDay = isAllDay
    )
}
