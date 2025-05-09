package gomushin.backend.schedule.dto.response

import gomushin.backend.schedule.domain.entity.Letter
import gomushin.backend.schedule.domain.entity.Picture
import gomushin.backend.schedule.domain.entity.Schedule
import java.time.LocalDateTime

data class LetterPreviewResponse(
    val letterId: Long?,
    val scheduleId: Long?,
    val scheduleTitle: String?,
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
            schedule: Schedule?,
            picture: Picture?
        ): LetterPreviewResponse {
            val previewContent = if (letter?.content != null && letter.content.length > MAX_CONTENT_LENGTH) {
                letter.content.take(PREVIEW_CONTENT_LENGTH) + "..."
            } else {
                letter?.content
            }
            return LetterPreviewResponse(
                letterId = letter?.id,
                scheduleId = schedule?.id,
                scheduleTitle = schedule?.title,
                title = letter?.title,
                content = previewContent,
                pictureUrl = picture?.pictureUrl,
                createdAt = letter?.createdAt
            )
        }
    }
}
