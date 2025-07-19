package gomushin.backend.couple.dto.response

import gomushin.backend.couple.domain.entity.Anniversary
import java.time.LocalDate

data class MainAnniversaryResponse(
    val id: Long,
    val emoji: String,
    val title: String,
    val anniversaryDate: LocalDate,
) {
    companion object {
        fun of(
            anniversary: Anniversary
        ): MainAnniversaryResponse {
            return MainAnniversaryResponse(
                id = anniversary.id,
                emoji = anniversary.emoji!!.name,
                title = anniversary.title,
                anniversaryDate = anniversary.anniversaryDate
            )
        }
    }
}
