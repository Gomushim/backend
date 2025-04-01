package gomushin.backend.core.configuration.env

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class AppEnv(
    @Value("\${spring.application.name}")
    private val applicationName: String
) {
    fun getId(): String = applicationName
}
