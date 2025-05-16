package gomushin.backend.member.dto.response

import gomushin.backend.member.domain.entity.Member
import gomushin.backend.member.domain.value.Emotion

data class MyEmotionResponse (
    val emotion : Emotion?
) {
    companion object {
        fun of(member: Member) = MyEmotionResponse (
            member.emotion
        )
    }
}