package gomushin.backend.member.dto.response

import gomushin.backend.member.domain.entity.Notification
import io.swagger.v3.oas.annotations.media.Schema

data class MyNotificationResponse(
    @Schema(description = "디데이 알림 설정 정보", example = "true")
    val dday : Boolean,
    @Schema(description = "상태 알림 설정 정보", example = "false")
    val partnerStatus : Boolean
) {
    companion object {
        fun of(notification: Notification) = MyNotificationResponse (
            notification.dday,
            notification.partnerStatus
        )
    }
}
