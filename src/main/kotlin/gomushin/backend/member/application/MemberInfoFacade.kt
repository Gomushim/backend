package gomushin.backend.member.application

import gomushin.backend.core.CustomUserDetails
import gomushin.backend.member.domain.service.MemberInfoService
import gomushin.backend.member.presentation.dto.response.MyInfoResponse
import org.springframework.stereotype.Component

@Component
class MemberInfoFacade(
    private val memberInfoService: MemberInfoService,
) {
    fun getMemberInfo(customUserDetails: CustomUserDetails): MyInfoResponse {
        val member = memberInfoService.getById(customUserDetails.getId())
        return MyInfoResponse.of(member)
    }
}
