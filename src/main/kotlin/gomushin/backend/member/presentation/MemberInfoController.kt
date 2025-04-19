package gomushin.backend.member.presentation

import gomushin.backend.core.CustomUserDetails
import gomushin.backend.core.common.web.response.ApiResponse
import gomushin.backend.member.facade.MemberInfoFacade
import gomushin.backend.member.dto.response.MyInfoResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
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
}
