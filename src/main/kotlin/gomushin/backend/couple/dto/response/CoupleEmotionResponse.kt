package gomushin.backend.couple.dto.response

data class CoupleEmotionResponse (
    val emotion : Int
) {
    companion object{
        fun of(emotion: Int) = CoupleEmotionResponse(
            emotion
        )
    }
}