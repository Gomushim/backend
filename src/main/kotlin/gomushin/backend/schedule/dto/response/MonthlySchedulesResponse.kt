package gomushin.backend.schedule.dto.response

import java.time.LocalDateTime

data class MonthlySchedulesResponse(
    val title: String,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val fatigue: String,
)
