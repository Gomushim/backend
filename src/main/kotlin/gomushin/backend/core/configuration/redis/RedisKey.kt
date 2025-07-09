package gomushin.backend.core.configuration.redis

enum class RedisKey(
    private val tag : String
) {
    ALARM("alarm:"),
    REFRESH_TOKEN("refresh_token:");

    companion object {
        fun getRedisAlarmKey(receiverId : Long) : String {
            return ALARM.tag + receiverId
        }
        fun getRedisRefreshKey(refreshToken : String) : String {
            return REFRESH_TOKEN.tag + refreshToken
        }
    }
}