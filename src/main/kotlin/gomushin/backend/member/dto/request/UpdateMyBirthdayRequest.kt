package gomushin.backend.member.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

data class UpdateMyBirthdayRequest (
    @Schema(description = "생일", example = "2001-03-27")
    val birthDate : LocalDate
)