package gomushin.backend.auth.presentation

import gomushin.backend.auth.facade.LogoutFacade
import gomushin.backend.core.common.web.response.ApiResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "로그아웃", description = "LogoutController")
class LogoutController(
    private val logoutFacade: LogoutFacade
) {

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping(ApiPath.LOGOUT)
    @Operation(summary = "로그아웃", description = "logout")
    fun logout(
        request: HttpServletRequest,
        response: HttpServletResponse,
    ):ApiResponse<Boolean> {
        logoutFacade.logout(request, response)
        return ApiResponse.success(true)
    }
}
