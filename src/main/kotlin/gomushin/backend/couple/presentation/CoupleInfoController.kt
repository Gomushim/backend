package gomushin.backend.couple.presentation

import gomushin.backend.core.CustomUserDetails
import gomushin.backend.core.common.web.response.ApiResponse
import gomushin.backend.couple.dto.response.*
import gomushin.backend.couple.facade.CoupleFacade
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
    @Operation(summary = "grade 조회 api", description = "getGrade")
    fun getGrade(
            @AuthenticationPrincipal customUserDetails: CustomUserDetails
    ): ApiResponse<CoupleGradeResponse> {
        val grade = coupleFacade.getGradeInfo(customUserDetails)
        return ApiResponse.success(grade)
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(ApiPath.COUPLE_CHECK_CONNECT)
    @Operation(summary = "커플 연동 여부 체크 api", description = "coupleConnectCheck")
    fun coupleConnectCheck(
            @AuthenticationPrincipal customUserDetails: CustomUserDetails
    ): ApiResponse<Boolean> {
        return ApiResponse.success(coupleFacade.checkConnect(customUserDetails))
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(ApiPath.COUPLE_DDAY_INFO)
    @Operation(summary = "디데이 정보 조회 api", description = "getDday")
    fun getDday(
            @AuthenticationPrincipal customUserDetails: CustomUserDetails
    ):ApiResponse<DdayResponse> {
        return ApiResponse.success(coupleFacade.getDday(customUserDetails))
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(ApiPath.COUPLE_NICKNAME)
    @Operation(summary = "닉네임 조회 api", description = "getNickName")
    fun getNickName(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails
    ):ApiResponse<NicknameResponse>{
        return ApiResponse.success(coupleFacade.nickName(customUserDetails))
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(ApiPath.COUPLE_STATUS_MESSAGE)
    @Operation(summary = "상태 메시지 조회 api", description = "getStatusMessage")
    fun getStatusMessage(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails
    ):ApiResponse<StatusMessageResponse>{
        return ApiResponse.success(coupleFacade.statusMessage(customUserDetails))
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(ApiPath.COUPLE_EMOJI)
    @Operation(summary = "커플 이모지 조회 api", description = "getCoupleEmotion")
    fun getCoupleEmotion(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails
    ):ApiResponse<CoupleEmotionResponse>{
        return ApiResponse.success(coupleFacade.getCoupleEmotion(customUserDetails))
    }
}