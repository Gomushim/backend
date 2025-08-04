package gomushin.backend.couple.dto.request

import gomushin.backend.couple.domain.value.AnniversaryEmoji
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

data class GenerateAnniversaryRequest(
    @Schema(description = "제목", example = "전역일")
    val title : String,
    @Schema(description = "이모지", example = "HEART, CALENDAR, CAKE, TRAVEL")
    val emoji : AnniversaryEmoji,
    @Schema(description = "날짜", example = "2025-05-01")
    val date : LocalDate,
)
