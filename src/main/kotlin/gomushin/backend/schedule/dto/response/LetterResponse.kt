package gomushin.backend.schedule.dto.response

import gomushin.backend.schedule.domain.entity.Letter
import java.time.LocalDateTime

data class LetterResponse(
    val id: Long,
    val title: String,
    val content: String,
    val author: String,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun of(letter: Letter): LetterResponse {
            return LetterResponse(
                id = letter.id,
                title = letter.title,
                content = letter.content,
                author = letter.author,
                createdAt = letter.createdAt,
            )
        }
    }
}
