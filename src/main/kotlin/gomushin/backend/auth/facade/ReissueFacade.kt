package gomushin.backend.auth.facade

import gomushin.backend.core.configuration.cookie.CookieService
import gomushin.backend.core.jwt.infrastructure.TokenService
import gomushin.backend.member.domain.service.MemberService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component

@Component
class ReissueFacade(
    private val tokenService: TokenService,
    private val memberService: MemberService,
    private val cookieService: CookieService
){
    fun reissue(refreshToken : String, response: HttpServletResponse) {
        tokenService.validateToken(refreshToken)
        val userId = tokenService.getRefreshTokenValue(refreshToken)
        tokenService.deleteRefreshToken(refreshToken)
        val user = memberService.getById(userId)
        val newAccessToken = tokenService.provideAccessToken(userId, user.role.name)
        val newRefreshToken = tokenService.provideRefreshToken()
        tokenService.upsertRefresh(userId, newRefreshToken, tokenService.getTokenDuration(newAccessToken))
        val accessCookie = cookieService.createCookie("access_token", newAccessToken)
        val refreshCookie = cookieService.createCookie("refresh_token", newRefreshToken)
        response.addHeader("Set-Cookie", accessCookie.toString())
        response.addHeader("Set-Cookie", refreshCookie.toString())
    }
}
