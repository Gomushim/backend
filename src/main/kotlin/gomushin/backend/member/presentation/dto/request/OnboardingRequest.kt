package gomushin.backend.member.presentation.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

data class OnboardingRequest(
    @Schema(description = "닉네임", example = "nickname")
    val nickname: String,

    @Schema(description = "생일", example = "2000-01-01")
    val birthDate: LocalDate,
)
