package gomushin.backend.core.jwt.infrastructure

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("jwt")
data class JwtProperties(
    val issuer: String,
    val audience: String,
    val secretKey: String,
    val accessTokenExpiration: Long,
    val refreshTokenExpiration: Long
)

