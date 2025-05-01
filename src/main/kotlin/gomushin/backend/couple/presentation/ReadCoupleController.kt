package gomushin.backend.couple.presentation

import gomushin.backend.core.CustomUserDetails
import gomushin.backend.core.common.web.response.ApiResponse
import gomushin.backend.couple.dto.response.CoupleInfoResponse
import gomushin.backend.couple.facade.CoupleFacade
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "커플 조회", description = "ReadCoupleController")
class ReadCoupleController(
    private val coupleFacade: CoupleFacade,
) {

    @GetMapping(ApiPath.COUPLE)
    @Operation(
        summary = "커플 정보 조회",
        description = "getCoupleInfo"
    )
    fun getCoupleInfo(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails
    ): ApiResponse<CoupleInfoResponse> {
        return ApiResponse.success(coupleFacade.getInfo(customUserDetails))
    }
}
