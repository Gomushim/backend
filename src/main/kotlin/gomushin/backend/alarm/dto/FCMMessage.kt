package gomushin.backend.alarm.dto

data class FCMMessage(
    val validateOnly: Boolean,
    val message: Message
) {
    data class Message(
        val notification: Notification,
        val token: String
    )

    data class Notification(
        val title: String,
        val body: String,
        val image: String?
    )
}