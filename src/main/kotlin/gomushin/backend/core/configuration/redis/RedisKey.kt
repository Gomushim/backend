package gomushin.backend.core.configuration.redis

enum class RedisKey(
    private val tag : String
) {
    ALARM("alarm:");

    companion object {
        fun getRedisAlarmKey(receiverId : Long) : String {
            return ALARM.tag + receiverId
        }
    }
}