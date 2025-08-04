package gomushin.backend.core.configuration.jackson

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Configuration
class JacksonConfig {
    companion object {
        private const val DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"
        private const val DATE_FORMAT = "yyyy-MM-dd"
        private const val TIME_FORMAT = "HH:mm:ss.SSS"
        private val LOCAL_DATETIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)
        private val LOCAL_DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT)
        private val LOCAL_TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT)
    }

    @Bean
    fun objectMapper(): ObjectMapper {
        val javaTimeModule = JavaTimeModule().apply {
            addSerializer(LocalDateTime::class.java, LocalDateTimeSerializer(LOCAL_DATETIME_FORMATTER))
            addSerializer(LocalDate::class.java, LocalDateSerializer(LOCAL_DATE_FORMATTER))
            addSerializer(LocalTime::class.java, LocalTimeSerializer(LOCAL_TIME_FORMATTER))
        }

        return jacksonObjectMapper().apply {
            registerModules(javaTimeModule, KotlinModule.Builder().build())
            configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false)
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        }
    }
}
