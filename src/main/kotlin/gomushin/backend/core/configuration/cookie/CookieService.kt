package gomushin.backend.core.configuration.cookie

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseCookie
import org.springframework.stereotype.Service

@Service
class CookieService(
    @Value("\${cookie.domain}") private val cookieDomain: String
){
    fun createCookie(key: String, value: String): ResponseCookie {
        return ResponseCookie.from(key, value)
            .path("/")
            .httpOnly(true)
            .secure(true)
            .sameSite("None")
            .domain("localhost") //Todo push할때 cookie domain으로 바꾸기
            .maxAge(432000)
            .build()
    }
}