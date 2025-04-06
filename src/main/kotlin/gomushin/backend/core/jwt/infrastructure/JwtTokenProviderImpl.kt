package gomushin.backend.core.jwt.infrastructure

import gomushin.backend.core.jwt.JwtTokenProvider
import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtTokenProviderImpl(
    jwtProperties: JwtProperties
) : JwtTokenProvider {
    companion object {
        private val logger: Logger = LogManager.getLogger(JwtTokenProviderImpl::class.java)
    }

    val ISSUER = jwtProperties.issuer
    val AUDIENCE = jwtProperties.audience
    val SECRET_KEY = Keys.hmacShaKeyFor(jwtProperties.secretKey.toByteArray(Charsets.UTF_8))
    val ACCESS_TOKEN_EXPIRATION = jwtProperties.accessTokenExpiration
    val REFRESH_TOKEN_EXPIRATION = jwtProperties.refreshTokenExpiration

    override fun provideAccessToken(userId: Long): String {
        return createToken(userId, ACCESS_TOKEN_EXPIRATION, Type.ACCESS)
    }

    override fun getMemberIdFromToken(token: String): Long {
        val subject = getSubject(token)
        return subject.toLong()
    }

    override fun validateToken(token: String): Boolean {
        try {
            Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token)
            return true
        } catch (e: Exception) {
            when (e) {
                is ExpiredJwtException,
                is UnsupportedJwtException,
                is MalformedJwtException,
                is IllegalArgumentException -> return false

                else -> throw e
            }
        }
    }

    private fun createToken(userId: Long, expiration: Long, type: Type): String {
        val expirationMs = expiration * 60 * 1000
        val expiryDate = Date(System.currentTimeMillis() + expirationMs)

        return Jwts.builder()
            .issuer(ISSUER)
            .audience().add(AUDIENCE).and()
            .subject(userId.toString())
            .claim("type", type.name)
            .issuedAt(Date())
            .expiration(expiryDate)
            .signWith(SECRET_KEY)
            .compact()
    }

    fun getSubject(token: String): String {
        return Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).payload.subject
    }

//    private fun getClaimsFromToken(token: String): Claims {
//        try {
//            return Jwts.parserBuilder()
//                .setSigningKey(SECRET_KEY)
//                .build()
//                .parseClaimsJws(token)
//                .body
//        } catch (e: ExpiredJwtException) {
//            logger.warn("만료된 JWT 토큰입니다: {}", e.message)
//            throw e
//        } catch (e: UnsupportedJwtException) {
//            logger.warn("지원되지 않는 JWT 토큰입니다: {}", e.message)
//            throw e
//        } catch (e: MalformedJwtException) {
//            logger.warn("잘못된 형식의 JWT 토큰입니다: {}", e.message)
//            throw e
//        } catch (e: io.jsonwebtoken.security.SignatureException) {
//            logger.warn("유효하지 않은 JWT 서명입니다: {}", e.message)
//            throw e
//        } catch (e: Exception) {
//            logger.error("JWT 토큰 파싱 중 오류 발생: {}", e.message, e)
//            throw e
//        }
//    }
}
