package gomushin.backend.couple.dto.response

import gomushin.backend.couple.domain.entity.Anniversary
import java.time.LocalDate

data class AnniversaryDetailResponse(
    val id: Long,
    val title: String,
    val anniversaryDate: LocalDate,
) {
    companion object {
        fun of(anniversary: Anniversary): AnniversaryDetailResponse {
            return AnniversaryDetailResponse(
                id = anniversary.id,
                title = anniversary.title,
                anniversaryDate = anniversary.anniversaryDate,
            )
        }
    }
}
