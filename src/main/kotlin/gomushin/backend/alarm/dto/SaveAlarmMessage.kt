package gomushin.backend.alarm.dto

import java.time.LocalDateTime

data class SaveAlarmMessage(
    val title: String,
    val content: String,
    val timestamp: String = LocalDateTime.now().toString()
) {
    companion object {
        fun of(title: String, content: String) = SaveAlarmMessage(
            title,
            content,
        )
    }
}
