package gomushin.backend.couple.presentation

import gomushin.backend.core.CustomUserDetails
import gomushin.backend.core.common.web.response.ApiResponse
import gomushin.backend.couple.facade.CoupleFacade
import gomushin.backend.couple.dto.response.CoupleGradeResponse
import gomushin.backend.couple.dto.response.DdayResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "커플 정보 불러오기", description = "CoupleInfoController")
class CoupleInfoController (
        private val coupleFacade: CoupleFacade
) {
    @GetMapping(ApiPath.COUPLE_PROFILE)
    @Operation(summary = "프로필 조회", description = "입대일 날짜 기준으로 grade측정")
    fun profile(
            @AuthenticationPrincipal customUserDetails: CustomUserDetails
    ): ApiResponse<CoupleGradeResponse> {
        val grade = coupleFacade.getGradeInfo(customUserDetails)
        return ApiResponse.success(grade)
    }

    @GetMapping(ApiPath.COUPLE_CHECK_CONNECT)
    @Operation(summary = "커플 연동 여부", description = "커플 연동 여부 true, false로 불러오기")
    fun check(
            @AuthenticationPrincipal customUserDetails: CustomUserDetails
    ): ApiResponse<Boolean> {
        return ApiResponse.success(coupleFacade.checkConnect(customUserDetails))
    }

    @GetMapping(ApiPath.COUPLE_DDAY_INFO)
    @Operation(summary = "디데이 정보", description = "사귄지, 입대한지 얼마되었는지 그리고 전역까지 얼마나 남았는지")
    fun dDay(
            @AuthenticationPrincipal customUserDetails: CustomUserDetails
    ):ApiResponse<DdayResponse> {
        return ApiResponse.success(coupleFacade.getDday(customUserDetails))
    }
}