package gomushin.backend.member.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

data class OnboardingRequest(
    @Schema(description = "닉네임", example = "nickname")
    val nickname: String,

    @Schema(description = "생일", example = "2000-01-01")
    val birthDate: LocalDate,

    @Schema(description = "FCM 토큰")
    val fcmToken: String,

    @Schema(description = "알림 설정 여부", example = "false")
    val isNotification: Boolean,

    @Schema(description = "약관 동의 여부", example = "true")
    val isTermsAgreed: Boolean
)
