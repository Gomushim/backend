package gomushin.backend.schedule.dto.response

import java.time.LocalDate

data class DailyAnniversaryResponse(
    val id: Long,
    val title: String,
    val anniversaryDate: LocalDate,
)
