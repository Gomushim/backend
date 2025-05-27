package gomushin.backend.alarm.dto

data class FCMMessage(
    val validateOnly: Boolean,
    val message: Message
) {
    data class Message(
        val notification: Notification,
        val token: String,
        val webpush : Webpush?
    )

    data class Notification(
        val title: String,
        val body: String,
        val image: String?
    )

    data class Webpush(
        val fcm_options : Fcm_options?
    )

    data class Fcm_options(
        val link : String?
    )
}