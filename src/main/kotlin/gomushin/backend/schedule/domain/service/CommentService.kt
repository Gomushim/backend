package gomushin.backend.schedule.domain.service

import gomushin.backend.core.infrastructure.exception.BadRequestException
import gomushin.backend.schedule.domain.entity.Comment
import gomushin.backend.schedule.domain.repository.CommentRepository
import gomushin.backend.schedule.dto.UpsertCommentRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CommentService(
    private val commentRepository: CommentRepository,
) {
    @Transactional
    fun upsert(
        id: Long?,
        letterId: Long,
        authorId: Long,
        nickname: String,
        upsertCommentRequest: UpsertCommentRequest
    ) {
        id?.let { commentId ->
            getById(commentId).let {
                if (it.authorId != authorId) {
                    throw BadRequestException("sarangggun.comment.unauthorized")
                }
                it.content = upsertCommentRequest.content
            }
        } ?: save(
            Comment.of(
                letterId = letterId,
                authorId = authorId,
                nickname = nickname,
                content = upsertCommentRequest.content,
            )
        )
    }

    @Transactional(readOnly = true)
    fun getById(id: Long): Comment {
        return findById(id) ?: throw BadRequestException("sarangggun.comment.not-found")
    }

    @Transactional(readOnly = true)
    fun findById(id: Long): Comment? {
        return commentRepository.findByIdOrNull(id)
    }

    @Transactional
    fun save(comment: Comment): Comment {
        return commentRepository.save(comment)
    }

    @Transactional
    fun delete(id: Long) {
        commentRepository.deleteById(id)
    }

    @Transactional
    fun deleteAllByMemberId(memberId: Long) {
        commentRepository.deleteAllByAuthorId(memberId)
    }
}
