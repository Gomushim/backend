package gomushin.backend.core.configuration.redis

import com.fasterxml.jackson.databind.ObjectMapper
import gomushin.backend.alarm.dto.SaveAlarmMessage
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class RedisService (
    private val redisTemplate: StringRedisTemplate,
    private val objectMapper: ObjectMapper
) {
    fun saveAlarm(title : String, content : String, receiverId : Long) {
        val savedAlarmMessage = SaveAlarmMessage.of(title, content)
        val key = RedisKey.getRedisAlarmKey(receiverId)
        val json = objectMapper.writeValueAsString(savedAlarmMessage)
        redisTemplate.opsForList().leftPush(key, json)
    }

    fun getAlarms(memberId : Long, recentDays : Long) : List<SaveAlarmMessage> {
        val key = RedisKey.getRedisAlarmKey(memberId)
        val allJson = redisTemplate.opsForList().range(key, 0, -1) ?: return emptyList()
        val now = LocalDateTime.now()
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
        val cutoff = now.minusDays(recentDays)

        val validAlarms = mutableListOf<SaveAlarmMessage>()

        allJson.forEach { json ->
            val alarm = runCatching {
                objectMapper.readValue(json, SaveAlarmMessage::class.java)
            }.getOrNull() ?: return@forEach

            runCatching { LocalDateTime.parse(alarm.timestamp, formatter) }
                .getOrNull()
                ?.takeIf { it.isAfter(cutoff) }
                ?.let { validAlarms.add(alarm) }
                ?: run {
                    redisTemplate.opsForList().remove(key, 1, json)
                }
        }

        return validAlarms
    }

    fun upsertRefresh(userId : Long, refreshToken : String) {
        val key = RedisKey.getRedisRefreshKey(refreshToken)
        redisTemplate.opsForValue().set(key, userId.toString(), Duration.ofDays(1))
    }
}