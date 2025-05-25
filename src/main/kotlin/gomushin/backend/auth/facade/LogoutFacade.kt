package gomushin.backend.auth.facade

import gomushin.backend.auth.domain.service.AuthService
import gomushin.backend.core.jwt.infrastructure.TokenService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component

@Component
class LogoutFacade(
    private val authService: AuthService,
    private val tokenService: TokenService,
) {
    fun logout(
        request: HttpServletRequest,
        response: HttpServletResponse
    ) {
        val refreshToken = request.cookies?.find { it.name == "refresh_token" }?.value

        if (refreshToken != null) {
            tokenService.deleteRefreshToken(refreshToken)
        }

        authService.logout(response)
    }
}
