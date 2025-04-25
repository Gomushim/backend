package gomushin.backend.core.oauth.handler

import gomushin.backend.core.infrastructure.exception.BadRequestException
import gomushin.backend.core.jwt.JwtTokenProvider
import gomushin.backend.core.oauth.CustomOAuth2User
import gomushin.backend.member.domain.entity.Member
import gomushin.backend.member.domain.repository.MemberRepository
import io.jsonwebtoken.io.IOException
import jakarta.servlet.ServletException
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class CustomSuccessHandler(
    private val jwtTokenProvider: JwtTokenProvider,
    private val memberRepository: MemberRepository,
    @Value("\${redirect-url}") private val redirectUrl: String
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
        getMemberByEmail(principal.getEmail())?.let {
            accessToken = jwtTokenProvider.provideAccessToken(it.id, it.role.name)
        } ?: run {
            accessToken = jwtTokenProvider.provideAccessToken(principal.getUserId(), principal.getRole())
        }

        response!!.addCookie(createCookie("access_token", accessToken))
        response.sendRedirect(redirectUrl)
    }

    private fun createCookie(key: String, value: String): Cookie {
        val cookie = Cookie(key, value)
        cookie.path = "/"
        cookie.isHttpOnly = true
        cookie.secure = false //Todo : 밋업 할 때는 true로 변경하기
        cookie.maxAge = 1800
        cookie.setAttribute("SameSite", "Lax")
        return cookie
    }

    private fun getMemberByEmail(email: String): Member? {
        return memberRepository.findByEmail(email)
    }
}
