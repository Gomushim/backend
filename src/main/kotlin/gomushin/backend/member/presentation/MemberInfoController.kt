package gomushin.backend.member.presentation

import gomushin.backend.core.CustomUserDetails
import gomushin.backend.core.common.web.response.ApiResponse
import gomushin.backend.member.dto.request.UpdateMyBirthdayRequest
import gomushin.backend.member.dto.request.UpdateMyEmotionAndStatusMessageRequest
import gomushin.backend.member.dto.request.UpdateMyNickNameRequest
import gomushin.backend.member.dto.request.UpdateMyNotificationRequest
import gomushin.backend.member.dto.response.MyEmotionResponse
import gomushin.backend.member.facade.MemberInfoFacade
import gomushin.backend.member.dto.response.MyInfoResponse
import gomushin.backend.member.dto.response.MyStatusMessageResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "회원 정보", description = "MemberController")
class MemberInfoController(
    private val memberInfoFacade: MemberInfoFacade,
) {

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(ApiPath.MY_INFO)
    @Operation(summary = "내 정보 조회", description = "getMyInfo")
    fun get(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails
    ): ApiResponse<MyInfoResponse> {
        val member = memberInfoFacade.getMemberInfo(customUserDetails)
        return ApiResponse.success(member)
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(ApiPath.MY_STATUS_MESSAGE)
    @Operation(summary = "내 상태 메시지 조회", description = "getMyStatusMessage")
    fun getMyStatusMessage(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails
    ): ApiResponse<MyStatusMessageResponse> {
        val statusMessage = memberInfoFacade.getMyStatusMessage(customUserDetails)
        return ApiResponse.success(statusMessage)
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(ApiPath.UPDATE_MY_EMOTION_AND_STATUS_MESSAGE)
    @Operation(summary = "내 상태 이모지 및 상태 메시지 저장", description = "updateMyEmotionAndStatusMessage")
    fun updateMyEmotionAndStatusMessage(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @RequestBody updateMyEmotionAndStatusMessageRequest: UpdateMyEmotionAndStatusMessageRequest
    ): ApiResponse<Boolean> {
        memberInfoFacade.updateMyEmotionAndStatusMessage(customUserDetails, updateMyEmotionAndStatusMessageRequest)
        return ApiResponse.success(true)
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(ApiPath.MY_EMOTION)
    @Operation(summary = "내 상태 이모지 조회", description = "getMyEmotion")
    fun getMyEmotion(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails
    ):ApiResponse<MyEmotionResponse> {
        val emotion = memberInfoFacade.getMemberEmotion(customUserDetails)
        return ApiResponse.success(emotion)
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(ApiPath.UPDATE_MY_NICKNAME)
    @Operation(summary = "내 닉네임 수정", description = "updateMyNickname")
    fun updateMyNickname(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @RequestBody updateMyNickNameRequest: UpdateMyNickNameRequest
    ):ApiResponse<Boolean> {
        memberInfoFacade.updateMyNickname(customUserDetails, updateMyNickNameRequest)
        return ApiResponse.success(true)
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(ApiPath.UPDATE_MY_BIRTHDAY)
    @Operation(summary = "내 생일 수정", description = "updateMyBirthDate")
    fun updateMyBirthDate(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @RequestBody updateMyBirthdayRequest : UpdateMyBirthdayRequest
    ):ApiResponse<Boolean> {
        memberInfoFacade.updateMyBirthDate(customUserDetails, updateMyBirthdayRequest)
        return ApiResponse.success(true)
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(ApiPath.UPDATE_MY_NOTIFICATION_POLICY)
    @Operation(summary = "내 알림 정책 수정", description = "updateMyNotification")
    fun updateMyNotification(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @RequestBody updateMyNotificationRequest: UpdateMyNotificationRequest
    ):ApiResponse<Boolean> {
        memberInfoFacade.updateMyNotification(customUserDetails, updateMyNotificationRequest)
        return ApiResponse.success(true)
    }
}
