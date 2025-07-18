package gomushin.backend.auth.domain.service

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class AuthService(
    @Value("\${cookie.domain}")
    private val cookieDomain: String
) {
    companion object {
        private const val AT_PREFIX = "access_token"
        private const val RT_PREFIX = "refresh_token"
    }

    fun logout(response: HttpServletResponse) {
        val expiredAccess = Cookie(AT_PREFIX, "").apply {
            path = "/"
            domain = cookieDomain
            isHttpOnly = true
            secure = true
            maxAge = 0
        }

        val expiredRefresh = Cookie(RT_PREFIX, "").apply {
            path = "/"
            domain = cookieDomain
            isHttpOnly = true
            secure = true
            maxAge = 0
        }

        response.addCookie(expiredAccess)
        response.addCookie(expiredRefresh)
    }
}
