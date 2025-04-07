package gomushin.backend.core.oauth.handler

import gomushin.backend.core.jwt.JwtTokenProvider
import gomushin.backend.core.oauth.CustomOAuth2User
import gomushin.backend.member.domain.entity.Member
import gomushin.backend.member.domain.repository.MemberRepository
import io.jsonwebtoken.io.IOException
import jakarta.servlet.ServletException
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class CustomSuccessHandler(
    private val jwtTokenProvider: JwtTokenProvider,
    private val memberRepository: MemberRepository,
) : SimpleUrlAuthenticationSuccessHandler() {

    @Throws(IOException::class, ServletException::class)
    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication
    ) {
        val oAuth2User = authentication.principal as CustomOAuth2User
        var accessToken = ""
        getMemberByEmail(oAuth2User.getEmail())?.let {
            accessToken = jwtTokenProvider.provideAccessToken(it.id)
        } ?: run {
            accessToken = jwtTokenProvider.provideAccessToken(oAuth2User.getUserId())
        }

        response!!.addCookie(creatCookie("access_token", accessToken))
        response.sendRedirect("http://localhost:8080") // TODO: 프론트엔드 주소로 변경 , 환경변수 처리
    }

    private fun creatCookie(key: String, value: String): Cookie {
        val cookie = Cookie(key, value)
        cookie.path = "/"
        cookie.isHttpOnly = true
        cookie.secure = true
        cookie.maxAge = 1800
        return cookie
    }

    private fun getMemberByEmail(email: String): Member? {
        return memberRepository.findByEmail(email)
    }
}
