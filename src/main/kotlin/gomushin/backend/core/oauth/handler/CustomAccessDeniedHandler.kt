package gomushin.backend.core.oauth.handler

import com.fasterxml.jackson.databind.ObjectMapper
import gomushin.backend.core.common.web.response.ExtendedHttpStatus
import gomushin.backend.core.common.web.response.exception.ErrorCodeResolvingApiErrorException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component


@Component
class CustomAccessDeniedHandler(private val objectMapper: ObjectMapper) : AccessDeniedHandler {
    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException
    ) {
        if (response.isCommitted) return

        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.status = HttpServletResponse.SC_FORBIDDEN
        response.characterEncoding = "UTF-8"


        val errorCode = if (request.requestURI.contains("/v1/member/onboarding")) {
            "sarangggun.auth.guest-only"
        } else {
            "sarangggun.auth.member-only"
        }

        val exception = ErrorCodeResolvingApiErrorException(
            ExtendedHttpStatus.FORBIDDEN,
            errorCode
        )
        response.writer.write(
            objectMapper.writeValueAsString(exception.error)
        )

    }
}

