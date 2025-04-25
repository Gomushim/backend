package gomushin.backend.member.facade

import gomushin.backend.core.CustomUserDetails
import gomushin.backend.member.domain.service.MemberService
import gomushin.backend.member.dto.request.UpdateMyEmotionAndStatusMessageRequest
import gomushin.backend.member.dto.request.UpdateMyNickNameRequest
import gomushin.backend.member.dto.response.MyEmotionResponse
import gomushin.backend.member.dto.response.MyInfoResponse
import gomushin.backend.member.dto.response.MyStatusMessageResponse
import org.springframework.stereotype.Component

@Component
class MemberInfoFacade(
    private val memberService: MemberService,
) {
    fun getMemberInfo(customUserDetails: CustomUserDetails): MyInfoResponse {
        val member = memberService.getById(customUserDetails.getId())
        return MyInfoResponse.of(member)
    }

    fun getMyStatusMessage(customUserDetails: CustomUserDetails): MyStatusMessageResponse {
        val member = memberService.getById(customUserDetails.getId())
        return MyStatusMessageResponse.of(member)
    }

    fun updateMyEmotionAndStatusMessage(customUserDetails: CustomUserDetails, updateMyEmotionAndStatusMessageRequest: UpdateMyEmotionAndStatusMessageRequest)
        = memberService.updateMyEmotionAndStatusMessage(customUserDetails.getId(), updateMyEmotionAndStatusMessageRequest)

    fun getMemberEmotion(customUserDetails: CustomUserDetails): MyEmotionResponse {
        val member = memberService.getById(customUserDetails.getId())
        return MyEmotionResponse.of(member)
    }

    fun updateMyNickname(customUserDetails: CustomUserDetails, updateMyNickNameRequest: UpdateMyNickNameRequest)
        = memberService.updateMyNickname(customUserDetails.getId(), updateMyNickNameRequest)
}
