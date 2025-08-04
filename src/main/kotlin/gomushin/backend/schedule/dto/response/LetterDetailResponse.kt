package gomushin.backend.schedule.dto.response

data class LetterDetailResponse(
    val letter: LetterResponse,
    val pictures: List<PictureResponse>,
    val comments: List<CommentResponse>,
) {
    companion object {
        fun of(
            letter: LetterResponse,
            pictures: List<PictureResponse>,
            comments: List<CommentResponse>,
        ): LetterDetailResponse {
            return LetterDetailResponse(
                letter = letter,
                pictures = pictures,
                comments = comments,
            )
        }
    }
}
