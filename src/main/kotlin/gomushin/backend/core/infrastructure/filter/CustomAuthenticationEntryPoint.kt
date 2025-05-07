package gomushin.backend.core.infrastructure.filter

import com.fasterxml.jackson.databind.ObjectMapper
import gomushin.backend.core.common.web.response.ExtendedHttpStatus
import gomushin.backend.core.common.web.response.exception.ErrorCodeResolvingApiErrorException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class CustomAuthenticationEntryPoint: AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: AuthenticationException?
    ) {
        if (response != null) {
            if (response.isCommitted) return
        }

        response?.contentType = MediaType.APPLICATION_JSON_VALUE
        response?.status = HttpServletResponse.SC_UNAUTHORIZED
        response?.characterEncoding = "UTF-8"

        val exception = ErrorCodeResolvingApiErrorException(
            ExtendedHttpStatus.UNAUTHORIZED,
            "sarangggun.auth.unauthorized"
        )

        response?.writer?.write(
            ObjectMapper().writeValueAsString(exception.error)
        )
    }
}
