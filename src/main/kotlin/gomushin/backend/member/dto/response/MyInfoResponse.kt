package gomushin.backend.member.dto.response

import gomushin.backend.member.domain.entity.Member
import io.swagger.v3.oas.annotations.media.Schema

data class MyInfoResponse(
    @Schema(description = "내 닉네임", example = "닉네임")
    val nickname: String,
    @Schema(description = "커플 여부", example = "true")
    val isCouple: Boolean,
    @Schema(description = "온보딩 여부 (GUEST : 온보딩 X , MEMBER : 온보딩 O)", example = "MEMBER")
    val role: String
) {
    companion object {
        fun of(member: Member) = MyInfoResponse(
            member.nickname,
            member.isCouple,
            member.role.name
        )
    }
}
