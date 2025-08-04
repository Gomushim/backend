package gomushin.backend.couple.dto.response

interface AnniversaryNotificationInfo {
    val title: String
    val memberId: Long
    val fcmToken: String
}