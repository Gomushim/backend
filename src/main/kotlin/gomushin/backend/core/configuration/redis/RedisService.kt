package gomushin.backend.core.configuration.redis

import com.fasterxml.jackson.databind.ObjectMapper
import gomushin.backend.alarm.dto.SaveAlarmMessage
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service

@Service
class RedisService (
    private val redisTemplate: StringRedisTemplate
) {
    fun saveAlarm(title : String, content : String, receiverId : Long) {
        val savedAlarmMessage = SaveAlarmMessage.of(title, content)
        val key = RedisKey.getRedisAlarmKey(receiverId)
        val json = ObjectMapper().writeValueAsString(savedAlarmMessage)
        redisTemplate.opsForList().leftPush(key, json)
    }
}