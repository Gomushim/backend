package gomushin.backend.core.infrastructure.filter

import gomushin.backend.core.jwt.JwtTokenProvider
import gomushin.backend.core.service.CustomUserDetailsService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val tokenProvider: JwtTokenProvider,
    private val customUserDetailsService: CustomUserDetailsService
) : OncePerRequestFilter() {

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        return request.requestURI.contains("/v1/auth") || request.requestURI.contains("/v1/oauth") || request.requestURI.contains(
            "/swagger"
        ) || request.requestURI.contains("/v3/api-docs") || request.requestURI.contains("/api-docs")
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        val accessToken = getCookieValue(request, "access_token")

        when {
            accessToken != null && tokenProvider.validateToken(accessToken) -> {
                applyAuthentication(accessToken)
            }
        }

        filterChain.doFilter(request, response)
    }

    private fun applyAuthentication(token: String) {
        val userId = tokenProvider.getMemberIdFromToken(token)
        val userDetails = customUserDetailsService.loadUserById(userId)

        val auth = UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.authorities
        )
        SecurityContextHolder.getContext().authentication = auth
    }

    private fun getCookieValue(request: HttpServletRequest, name: String): String? {
        return request.cookies?.firstOrNull { it.name == name }?.value
    }
}
