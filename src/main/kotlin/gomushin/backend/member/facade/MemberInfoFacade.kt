package gomushin.backend.member.facade

import gomushin.backend.core.CustomUserDetails
import gomushin.backend.member.domain.service.MemberService
import gomushin.backend.member.dto.response.MyInfoResponse
import org.springframework.stereotype.Component

@Component
class MemberInfoFacade(
    private val memberService: MemberService,
) {
    fun getMemberInfo(customUserDetails: CustomUserDetails): MyInfoResponse {
        val member = memberService.getById(customUserDetails.getId())
        return MyInfoResponse.of(member)
    }
}
