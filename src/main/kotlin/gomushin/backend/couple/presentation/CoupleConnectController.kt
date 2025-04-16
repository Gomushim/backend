package gomushin.backend.couple.presentation

import gomushin.backend.core.CustomUserDetails
import gomushin.backend.core.common.web.response.ApiResponse
import gomushin.backend.couple.dto.request.CoupleConnectRequest
import gomushin.backend.couple.facade.CoupleFacade
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "커플 코드 생성", description = "CoupleConnectController")
class CoupleConnectController(
    private val coupleFacade: CoupleFacade
) {

    @PostMapping(ApiPath.COUPLE_CODE_GENERATE)
    @Operation(
        summary = "커플 코드 생성",
        description = "커플 코드를 생성합니다. 커플 코드는 60분 동안 유효합니다."
    )
    fun generateCoupleCode(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails
    ): ApiResponse<String> =
        ApiResponse.success(coupleFacade.requestCoupleCodeGeneration(customUserDetails))

    @PostMapping(ApiPath.COUPLE_CONNECT)
    @Operation(
        summary = "커플 코드 연결",
        description = "커플 코드를 통해 남자친구(여자친구)와 연결합니다."
    )
    fun connectCouple(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @RequestBody request: CoupleConnectRequest,
    ): ApiResponse<Boolean> {
        coupleFacade.requestCoupleConnect(customUserDetails, request)
        return ApiResponse.success(true)
    }
}
