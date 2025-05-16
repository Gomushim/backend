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
class CustomAuthenticationEntryPoint(private val objectMapper: ObjectMapper): AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: AuthenticationException?
    ) {

        if(response?.isCommitted == true) return

        response?.contentType = MediaType.APPLICATION_JSON_VALUE
        response?.status = HttpServletResponse.SC_UNAUTHORIZED
        response?.characterEncoding = "UTF-8"

        val exception = ErrorCodeResolvingApiErrorException(
            ExtendedHttpStatus.UNAUTHORIZED,
            "sarangggun.auth.unauthorized"
        )

        response?.writer?.write(
            objectMapper.writeValueAsString(exception.error)
        )
    }
}
