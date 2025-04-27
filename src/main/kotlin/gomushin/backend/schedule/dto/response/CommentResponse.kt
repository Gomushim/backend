package gomushin.backend.schedule.dto.response

import gomushin.backend.schedule.domain.entity.Comment
import java.time.LocalDateTime

data class CommentResponse(
    val id: Long,
    val content: String,
    val author: String,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun of(comment: Comment): CommentResponse {
            return CommentResponse(
                id = comment.id,
                content = comment.content,
                author = comment.nickname,
                createdAt = comment.createdAt,
            )
        }
    }
}
