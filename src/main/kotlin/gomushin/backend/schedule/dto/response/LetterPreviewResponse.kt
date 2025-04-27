package gomushin.backend.schedule.dto.response

import gomushin.backend.schedule.domain.entity.Letter
import gomushin.backend.schedule.domain.entity.Picture
import java.time.LocalDateTime

data class LetterPreviewResponse(
    val letterId: Long?,
    val title: String?,
    val content: String?,
    val pictureUrl: String?,
    val createdAt: LocalDateTime?,
) {
    companion object {
        private const val MAX_CONTENT_LENGTH = 30
        private const val PREVIEW_CONTENT_LENGTH = 27

        fun of(
            letter: Letter?,
            picture: Picture?
        ): LetterPreviewResponse {
            val previewContent = if (letter?.content != null && letter.content.length > MAX_CONTENT_LENGTH) {
                letter.content.take(PREVIEW_CONTENT_LENGTH) + "..."
            } else {
                letter?.content
            }
            return LetterPreviewResponse(
                letterId = letter?.id,
                title = letter?.title,
                content = previewContent,
                pictureUrl = picture?.pictureUrl,
                createdAt = letter?.createdAt
            )
        }
    }
}
