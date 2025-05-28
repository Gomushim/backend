package gomushin.backend.alarm.value

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "fcm.redirect")
data class RedirectURL (
    var main: String = "",
    var dday: String = ""
)