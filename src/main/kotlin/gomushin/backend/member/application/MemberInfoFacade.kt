package gomushin.backend.member.application

import gomushin.backend.core.CustomUserDetails
import gomushin.backend.member.domain.service.MemberInfoService
import gomushin.backend.member.presentation.dto.response.GuestInfoResponse
import org.springframework.stereotype.Component

@Component
class MemberInfoFacade(
    private val memberInfoService: MemberInfoService,
) {
    fun getMemberInfo(customUserDetails: CustomUserDetails): Any {
        val authorities = customUserDetails.authorities

//        if (authorities.any { it.authority == "ROLE_GUEST" }) {
        val member = memberInfoService.getGuestInfo(customUserDetails.getId())
        return GuestInfoResponse.of(member)
//        }

        // TODO: Member 구현 시 , 분기를 통해 MEMBER와 GUEST를 구분할 수 있도록 수정
    }
}
