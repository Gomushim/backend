package gomushin.backend.schedule.dto.response

import java.time.LocalDate

data class MainAnniversariesResponse(
    val id: Long,
    val title: String,
    val anniversaryDate: LocalDate,
)
