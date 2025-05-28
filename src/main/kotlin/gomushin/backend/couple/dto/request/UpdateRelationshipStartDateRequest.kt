package gomushin.backend.couple.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

data class UpdateRelationshipStartDateRequest (
    @Schema(description = "사귄날", example = "2023-05-24")
    val relationshipStartDate : LocalDate,
)