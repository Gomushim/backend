package gomushin.backend.core.jwt.infrastructure

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.*

@Component
class TokenService(
    jwtProperties: JwtProperties
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

    override fun provideRefreshToken() : String {
        return createToken(0, "", REFRESH_TOKEN_EXPIRATION, Type.REFRESH)
    }

    override fun getTokenDuration(token: String): Duration {
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
}
