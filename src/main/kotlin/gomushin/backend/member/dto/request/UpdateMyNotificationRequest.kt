package gomushin.backend.member.dto.request

import io.swagger.v3.oas.annotations.media.Schema

data class UpdateMyNotificationRequest (
    @Schema(description = "디데이 알림 설정", example = "true")
    val dday : Boolean,
    @Schema(description = "연인상태 알림 설정", example = "false")
    val partnerStatus : Boolean
)