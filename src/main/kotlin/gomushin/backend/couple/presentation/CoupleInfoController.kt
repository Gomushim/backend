package gomushin.backend.couple.presentation

import gomushin.backend.core.CustomUserDetails
import gomushin.backend.core.common.web.response.ApiResponse
import gomushin.backend.couple.facade.CoupleFacade
import gomushin.backend.couple.dto.response.CoupleGradeResponse
import gomushin.backend.couple.dto.response.DdayResponse
import gomushin.backend.couple.dto.response.NicknameResponse
import gomushin.backend.couple.dto.response.StatusMessageResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "커플 정보 불러오기", description = "CoupleInfoController")
class CoupleInfoController (
        private val coupleFacade: CoupleFacade
) {
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(ApiPath.COUPLE_PROFILE)
    @Operation(summary = "프로필 조회", description = "입대일 날짜 기준으로 grade측정")
    fun getGrade(
            @AuthenticationPrincipal customUserDetails: CustomUserDetails
    ): ApiResponse<CoupleGradeResponse> {
        val grade = coupleFacade.getGradeInfo(customUserDetails)
        return ApiResponse.success(grade)
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(ApiPath.COUPLE_CHECK_CONNECT)
    @Operation(summary = "커플 연동 여부", description = "커플 연동 여부 true, false로 불러오기")
    fun coupleConnectCheck(
            @AuthenticationPrincipal customUserDetails: CustomUserDetails
    ): ApiResponse<Boolean> {
        return ApiResponse.success(coupleFacade.checkConnect(customUserDetails))
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(ApiPath.COUPLE_DDAY_INFO)
    @Operation(summary = "디데이 정보", description = "사귄지, 입대한지 얼마되었는지 그리고 전역까지 얼마나 남았는지")
    fun getDday(
            @AuthenticationPrincipal customUserDetails: CustomUserDetails
    ):ApiResponse<DdayResponse> {
        return ApiResponse.success(coupleFacade.getDday(customUserDetails))
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(ApiPath.COUPLE_NICKNAME)
    @Operation(summary = "닉네임 조회", description = "userNickname = 내 닉네임, coupleNickName = 내 여(남)친 닉네임")
    fun getNickName(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails
    ):ApiResponse<NicknameResponse>{
        return ApiResponse.success(coupleFacade.nickName(customUserDetails))
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(ApiPath.COUPLE_STATUS_MESSAGE)
    @Operation(summary = "상태 메시지 조회", description = "상태 메시지 조회")
    fun getStatusMessage(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails
    ):ApiResponse<StatusMessageResponse>{
        return ApiResponse.success(coupleFacade.statusMessage(customUserDetails))
    }
}