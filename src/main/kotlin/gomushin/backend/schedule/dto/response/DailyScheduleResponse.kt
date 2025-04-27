package gomushin.backend.schedule.dto.response

import java.time.LocalDateTime

data class DailyScheduleResponse(
    val id: Long,
    val title: String,
    val fatigue: String,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val isAllDay: Boolean
)
