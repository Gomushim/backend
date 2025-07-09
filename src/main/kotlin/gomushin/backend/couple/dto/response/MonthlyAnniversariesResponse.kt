package gomushin.backend.couple.dto.response

import java.time.LocalDate

data class MonthlyAnniversariesResponse(
    val title: String,
    val anniversaryDate: LocalDate
)
