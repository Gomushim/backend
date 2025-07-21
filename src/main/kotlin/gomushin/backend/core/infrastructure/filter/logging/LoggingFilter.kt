package gomushin.backend.core.infrastructure.filter.logging

import gomushin.backend.core.CustomUserDetails
import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class LoggingFilter : Filter {

    private val log = LoggerFactory.getLogger(LoggingFilter::class.java)

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        if (request is HttpServletRequest) {
            if(request.contentType?.startsWith("multipart/") == true) {
                chain.doFilter(request, response)
                return
            }
            val wrappedRequest = CachedBodyHttpServletRequest(request)

            val url = wrappedRequest.requestURI
            val method = wrappedRequest.method
            val body = runCatching { wrappedRequest.reader.readLines().joinToString("") }.getOrNull()

            val authentication = SecurityContextHolder.getContext().authentication

            val userId: Long? = when {
                authentication == null || !authentication.isAuthenticated ||
                        authentication is AnonymousAuthenticationToken -> null
                authentication.principal is CustomUserDetails -> {
                    (authentication.principal as CustomUserDetails).getId()
                }
                else -> null
            }

            log.info("[REQUEST LOG] userId={}, URL={}, Method={}, Body={}", userId, url, method, body)

            chain.doFilter(wrappedRequest, response)
        } else {
            chain.doFilter(request, response)
        }
    }
}