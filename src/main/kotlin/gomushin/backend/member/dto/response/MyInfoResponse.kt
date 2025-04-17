package gomushin.backend.member.dto.response

import gomushin.backend.member.domain.entity.Member

data class MyInfoResponse(
    val nickname: String,
    val isCouple: Long,
) {
    companion object {
        fun of(member: Member) = MyInfoResponse(
            member.nickname,
            member.isCouple
        )
    }
}
