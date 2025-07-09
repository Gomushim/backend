package gomushin.backend.couple.dto.response

import gomushin.backend.couple.domain.entity.Anniversary
import java.time.LocalDate

data class TotalAnniversaryResponse(
    val id: Long?,
    val title: String?,
    val anniversaryDate: LocalDate?,
    val emoji: String?,
) {
    companion object {
        fun of(
            anniversary: Anniversary?
        ): TotalAnniversaryResponse {
            return TotalAnniversaryResponse(
                id = anniversary?.id,
                title = anniversary?.title,
                anniversaryDate = anniversary?.anniversaryDate,
                emoji = anniversary?.emoji.toString()
            )
        }
    }
}
