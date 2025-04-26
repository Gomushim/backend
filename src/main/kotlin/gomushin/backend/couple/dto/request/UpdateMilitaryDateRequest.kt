package gomushin.backend.couple.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

data class UpdateMilitaryDateRequest (
    @Schema(description = "입대일", example = "2023-05-24")
    val militaryStartDate : LocalDate,
    @Schema(description = "전역일", example = "2024-11-23")
    val militaryEndDate : LocalDate
)