package gomushin.backend.schedule.dto.response

import gomushin.backend.schedule.domain.entity.Letter
import java.time.LocalDateTime

data class LetterResponse(
    val id: Long,
    val title: String,
    val content: String,
    val author: String,
    val isWrittenByMe : Boolean,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun of(letter: Letter, memberId : Long): LetterResponse {
            return LetterResponse(
                id = letter.id,
                title = letter.title,
                content = letter.content,
                author = letter.author,
                isWrittenByMe = letter.authorId == memberId,
                createdAt = letter.createdAt,
            )
        }
    }
}
