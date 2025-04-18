package gomushin.backend.couple.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

class CoupleAnniversaryRequest(
    @Schema(description = "커플 ID", example = "1")
    val coupleId: Long,

    @Schema(description = "처음 만난 날", example = "2022-10-01")
    val relationshipStartDate: LocalDate,

    @Schema(description = "입대일", example = "2023-01-01")
    val militaryStartDate: LocalDate,

    @Schema(description = "전역일", example = "2024-10-28")
    val militaryEndDate: LocalDate,
)
