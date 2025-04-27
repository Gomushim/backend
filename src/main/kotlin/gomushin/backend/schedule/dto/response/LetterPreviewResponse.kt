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
        fun of(
            letter: Letter?,
            picture: Picture?
        ): LetterPreviewResponse {
            val previewContent = if (letter?.content != null && letter.content.length > 30) {
                letter.content.take(27) + "..."
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
