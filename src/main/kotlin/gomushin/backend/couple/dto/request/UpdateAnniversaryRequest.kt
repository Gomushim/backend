package gomushin.backend.couple.dto.request

import java.time.LocalDate

data class UpdateAnniversaryRequest(
    val title: String? = null,
    val emoji: String? = null,
    val anniversaryDate: LocalDate? = null
)
