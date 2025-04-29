package gomushin.backend.couple.dto.response

import gomushin.backend.member.domain.value.Emotion

data class CoupleEmotionResponse (
    val emotion : String
) {
    companion object{
        fun of(emotion: Emotion) = CoupleEmotionResponse(
            emotion.name
        )
    }
}