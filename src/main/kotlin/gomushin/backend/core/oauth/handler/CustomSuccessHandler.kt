package gomushin.backend.core.oauth.handler

import gomushin.backend.core.configuration.cookie.CookieService
import gomushin.backend.core.infrastructure.exception.BadRequestException
import gomushin.backend.core.jwt.infrastructure.TokenService
import gomushin.backend.core.oauth.CustomOAuth2User
import gomushin.backend.member.domain.entity.Member
import gomushin.backend.member.domain.repository.MemberRepository
import gomushin.backend.member.domain.value.Role
import io.jsonwebtoken.io.IOException
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class CustomSuccessHandler(
    private val tokenService: TokenService,
    private val memberRepository: MemberRepository,
    private val cookieService: CookieService,
    @Value("\${member-redirect-url}") private val memberRedirectUrl: String,
    @Value("\${guest-redirect-url}") private val guestRedirectUrl: String,
) : SimpleUrlAuthenticationSuccessHandler() {

    @Throws(IOException::class, ServletException::class)
    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication
    ) {
        val principal = authentication.principal

        if (principal !is CustomOAuth2User) {
            throw BadRequestException("sarangggun.oauth.invalid-principal")
        }

        var accessToken = ""
        val refreshToken = tokenService.provideRefreshToken()

        getMemberByEmail(principal.getEmail())?.let {
            accessToken = tokenService.provideAccessToken(it.id, it.role.name)
            tokenService.upsertRefresh(it.id, refreshToken, tokenService.getTokenDuration(refreshToken))
        } ?: run {
            accessToken = tokenService.provideAccessToken(principal.getUserId(), principal.getRole())
            tokenService.upsertRefresh(principal.getUserId(), refreshToken, tokenService.getTokenDuration(refreshToken))
        }

        val accessCookie = cookieService.createCookie("access_token", accessToken)
        val refreshCookie = cookieService.createCookie("refresh_token", refreshToken)
        response!!.addHeader("Set-Cookie", accessCookie.toString())
        response.addHeader("Set-Cookie", refreshCookie.toString())
        response.sendRedirect(memberRedirectUrl)
    }

    private fun getMemberByEmail(email: String): Member? {
        return memberRepository.findByEmail(email)
    }

}
