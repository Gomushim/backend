package gomushin.backend.core.infrastructure.filter

import gomushin.backend.core.jwt.JwtTokenProvider
import gomushin.backend.core.CustomUserDetailsService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val tokenProvider: JwtTokenProvider,
    private val customUserDetailsService: CustomUserDetailsService
) : OncePerRequestFilter() {
    private val AUTHORIZATION_HEADER = "Authorization"
    private val BEARER_PREFIX = "Bearer "

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        return request.requestURI.contains("/v1/auth") || request.requestURI.contains("/v1/oauth")
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val token = resolveToken(request)
            if (token.isNotEmpty()) {
                val auth = createAuthentication(token)
                SecurityContextHolder.getContext().authentication = auth
            }
            filterChain.doFilter(request, response)
        } catch (e: Exception) {
            SecurityContextHolder.clearContext()
            response.sendError(401, "로그인이 필요한 서비스입니다.")
        }
    }

    private fun resolveToken(request: HttpServletRequest): String {
        val authHeader = request.getHeader(AUTHORIZATION_HEADER)
        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            return authHeader.substring(7)
        }
        return ""
    }

    private fun createAuthentication(token: String): Authentication {
        val userDetails = customUserDetailsService.loadUserById(tokenProvider.getMemberIdFromToken(token))
        return UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
    }
}
