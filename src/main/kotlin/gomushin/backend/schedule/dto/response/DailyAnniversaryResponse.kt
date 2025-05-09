package gomushin.backend.schedule.dto.response

import gomushin.backend.couple.domain.value.AnniversaryEmoji
import java.time.LocalDate

data class DailyAnniversaryResponse(
    val id: Long,
    val title: String,
    val emoji: AnniversaryEmoji,
    val anniversaryDate: LocalDate,
)
