package gomushin.backend.auth.presentation

import gomushin.backend.auth.application.AuthorizeOAuthUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import java.net.URI

@Tag(name = "OAuth", description = "OAuth 로그인 API")
@Controller
class AuthorizeOAuthController(
    private val authorizeOAuthUseCase: AuthorizeOAuthUseCase
) {
    @Operation(summary = "OAuth 로그인", description = "OAuth 로그인 하는 url.")
    @ApiResponses(
        ApiResponse(responseCode = "201", description = "로그인 성공"),
        ApiResponse(responseCode = "400", description = "잘못된 요청"),
        ApiResponse(responseCode = "500", description = "서버 오류")
    )
    @GetMapping(ApiPath.OAuth.AUTHORIZE_OAUTH)
    fun authorizeOAuth(@PathVariable provider: String): ResponseEntity<Void> {
        return ResponseEntity.status(HttpStatus.FOUND)
            .location(URI.create(authorizeOAuthUseCase.getRedirectUrl(provider)))
            .build()
    }
}
