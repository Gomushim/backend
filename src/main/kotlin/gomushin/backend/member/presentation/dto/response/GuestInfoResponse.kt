package gomushin.backend.member.presentation.dto.response

import gomushin.backend.member.domain.entity.Member

data class GuestInfoResponse(
    val nickname: String,
) {
    companion object {
        fun of(member: Member) = GuestInfoResponse(member.nickname)
    }
}
