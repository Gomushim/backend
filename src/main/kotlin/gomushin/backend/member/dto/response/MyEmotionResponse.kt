package gomushin.backend.member.dto.response

import gomushin.backend.member.domain.entity.Member

data class MyEmotionResponse (
    val emotion : Int?
) {
    companion object {
        fun of(member: Member) = MyEmotionResponse (
            member.emotion
        )
    }
}