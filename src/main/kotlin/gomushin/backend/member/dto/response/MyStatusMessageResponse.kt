package gomushin.backend.member.dto.response

import gomushin.backend.member.domain.entity.Member

data class MyStatusMessageResponse (
    val statusMessage : String?
) {
    companion object {
        fun of(member: Member) = MyStatusMessageResponse(
            member.statusMessage
        )
    }
}