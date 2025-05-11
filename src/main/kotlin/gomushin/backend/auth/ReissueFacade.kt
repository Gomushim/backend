package gomushin.backend.auth

import gomushin.backend.core.configuration.cookie.CookieService
import gomushin.backend.core.configuration.redis.RedisKey
import gomushin.backend.core.configuration.redis.RedisService
import gomushin.backend.core.jwt.JwtTokenProvider
import gomushin.backend.core.jwt.infrastructure.JwtProperties
import gomushin.backend.member.domain.service.MemberService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component

@Component
class ReissueFacade(
    private val redisService: RedisService,
    private val jwtTokenProvider: JwtTokenProvider,
    private val memberService: MemberService,
    private val cookieService: CookieService
){
    fun reissue(refreshToken : String, response: HttpServletResponse) {
        jwtTokenProvider.validateToken(refreshToken)
        val userId = redisService.getRefreshTokenValue(refreshToken)
        redisService.deleteRefreshToken(refreshToken)
        val user = memberService.getById(userId)
        val newAccessToken = jwtTokenProvider.provideAccessToken(userId, user.role.name)
        val newRefreshToken = jwtTokenProvider.provideRefreshToken()
        redisService.upsertRefresh(userId, newRefreshToken, jwtTokenProvider.getTokenDuration(newAccessToken))
        val accessCookie = cookieService.createCookie("access_token", newAccessToken)
        val refreshCookie = cookieService.createCookie("refresh_token", newRefreshToken)
        response.addHeader("Set-Cookie", accessCookie.toString())
        response.addHeader("Set-Cookie", refreshCookie.toString())
    }
}