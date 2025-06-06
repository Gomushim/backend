package gomushin.backend.core.jwt.infrastructure

import gomushin.backend.core.configuration.redis.RedisKey
import gomushin.backend.core.infrastructure.exception.BadRequestException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.*

@Component
class TokenService(
    jwtProperties: JwtProperties,
    private val redisTemplate: StringRedisTemplate
) {
    companion object {
        private val logger: Logger = LogManager.getLogger(TokenService::class.java)
    }

    val ISSUER = jwtProperties.issuer
    val AUDIENCE = jwtProperties.audience
    val SECRET_KEY = Keys.hmacShaKeyFor(jwtProperties.secretKey.toByteArray(Charsets.UTF_8))
    val ACCESS_TOKEN_EXPIRATION = jwtProperties.accessTokenExpiration
    val REFRESH_TOKEN_EXPIRATION = jwtProperties.refreshTokenExpiration

    fun provideAccessToken(userId: Long, role: String): String {
        return createToken(userId, role, ACCESS_TOKEN_EXPIRATION, Type.ACCESS)
    }

    fun getMemberIdFromToken(token: String): Long {
        val subject = getSubject(token)
        return subject.toLong()
    }

    fun validateToken(token: String): Boolean {
        try {
            Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token)
            return true
        } catch (e: Exception) {
            return false
        }
    }

    fun provideRefreshToken() : String {
        return createToken(0, "", REFRESH_TOKEN_EXPIRATION, Type.REFRESH)
    }

    fun getTokenDuration(token: String): Duration {
        val now = Date(System.currentTimeMillis())
        return Duration.between(now.toInstant(), getTokenExpiration(token).toInstant())
    }

    private fun getTokenExpiration(token: String) : Date {
        return Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).payload.expiration
    }

    private fun createToken(userId: Long, role: String, expiration: Long, type: Type): String {
        val expirationMs = expiration * 60 * 1000
        val expiryDate = Date(System.currentTimeMillis() + expirationMs)

        return Jwts.builder()
            .issuer(ISSUER)
            .audience().add(AUDIENCE).and()
            .subject(userId.toString())
            .claim("type", type.name)
            .claim("role", role)
            .issuedAt(Date())
            .expiration(expiryDate)
            .signWith(SECRET_KEY)
            .compact()
    }

    fun getSubject(token: String): String {
        return Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).payload.subject
    }

    fun upsertRefresh(userId : Long, refreshToken : String, duration: Duration) {
        val key = RedisKey.getRedisRefreshKey(refreshToken)
        redisTemplate.opsForValue().set(key, userId.toString(), duration)
    }

    fun getRefreshTokenValue(refreshToken: String) : Long {
        val key = RedisKey.getRedisRefreshKey(refreshToken)
        val value = redisTemplate.opsForValue().get(key)
            ?: throw BadRequestException("sarangggun.auth.invalid-refresh")
        return value.toLong()
    }

    fun deleteRefreshToken(refreshToken: String) {
        val key = RedisKey.getRedisRefreshKey(refreshToken)
        redisTemplate.delete(key)
    }
}
