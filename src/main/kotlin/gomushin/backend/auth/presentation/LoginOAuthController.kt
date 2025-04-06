package gomushin.backend.auth.presentation

import gomushin.backend.auth.application.LoginOAuthUseCase
import gomushin.backend.auth.presentation.response.LoginOAuthResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "OAuth", description = "OAuth 로그인 API")
@RestController
class LoginOAuthController(
    private val loginOAuthUseCase: LoginOAuthUseCase
) {
    @Operation(summary = "OAuth 로그인", description = "OAuth 코드 보내는 URL")
    @GetMapping(ApiPath.OAuth.LOGIN_OAUTH)
    fun loginOAuth(
        @PathVariable provider: String,
        @RequestParam("code", required = false) code: String?,
        @RequestParam("state", required = false) state: String?,
        @RequestParam("error", required = false) error: String?,
        @RequestParam("error_description", required = false) errorDescription: String?
    ): LoginOAuthResponse = loginOAuthUseCase.execute(provider, code, state, error, errorDescription)
}
