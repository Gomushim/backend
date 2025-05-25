package gomushin.backend.auth.presentation

import gomushin.backend.auth.facade.ReissueFacade
import gomushin.backend.core.common.web.response.ApiResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@Tag(name = "토큰 재발급", description = "ReissueController")
class ReissueController (
    private val reissueFacade: ReissueFacade
) {
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(ApiPath.REISSUE)
    @Operation(summary = "토큰 재발급", description = "reissue")
    fun reissue(
        @CookieValue("refresh_token") refreshToken: String,
        response : HttpServletResponse
    ): ApiResponse<Boolean> {
        reissueFacade.reissue(refreshToken, response)
        return ApiResponse.success(true)
    }
}
