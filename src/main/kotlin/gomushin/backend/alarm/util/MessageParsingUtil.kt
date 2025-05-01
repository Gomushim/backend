package gomushin.backend.alarm.util

object MessageParsingUtil {
    fun parse(notificationContent : String) : Pair<String, String> {
        val parts = notificationContent.split("+")
        val title = parts[0]
        val content = parts[1]
        return title to content
    }
}