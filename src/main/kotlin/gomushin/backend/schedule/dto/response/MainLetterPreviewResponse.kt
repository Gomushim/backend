package gomushin.backend.schedule.dto.response

import gomushin.backend.schedule.domain.entity.Letter
import gomushin.backend.schedule.domain.entity.Picture
import gomushin.backend.schedule.domain.entity.Schedule
import java.time.LocalDateTime

data class MainLetterPreviewResponse(
    val letterId: Long?,
    val title: String?,
    val content: String?,
    val pictureUrl: String?,
    val schedule: String?,
    val createdAt: LocalDateTime?,
) {
    companion object {
        private const val MAX_CONTENT_LENGTH = 30
        private const val PREVIEW_CONTENT_LENGTH = 27

        fun of(
            letter: Letter?,
            picture: Picture?,
            schedule: Schedule?,
        ): MainLetterPreviewResponse {
            val previewContent = if (letter?.content != null && letter.content.length > MAX_CONTENT_LENGTH) {
                letter.content.take(PREVIEW_CONTENT_LENGTH) + "..."
            } else {
                letter?.content
            }
            return MainLetterPreviewResponse(
                letterId = letter?.id,
                title = letter?.title,
                content = previewContent,
                pictureUrl = picture?.pictureUrl,
                schedule = schedule?.title,
                createdAt = letter?.createdAt
            )
        }
    }
}
