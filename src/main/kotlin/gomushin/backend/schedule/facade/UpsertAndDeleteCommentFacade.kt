package gomushin.backend.schedule.facade

import gomushin.backend.core.CustomUserDetails
import gomushin.backend.core.infrastructure.exception.BadRequestException
import gomushin.backend.member.domain.service.MemberService
import gomushin.backend.schedule.domain.service.CommentService
import gomushin.backend.schedule.domain.service.LetterService
import gomushin.backend.schedule.dto.UpsertCommentRequest
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class UpsertAndDeleteCommentFacade(
    private val commentService: CommentService,
    private val letterService: LetterService,
    private val memberService: MemberService,
) {
    @Transactional
    fun upsert(customUserDetails: CustomUserDetails, letterId: Long, upsertCommentRequest: UpsertCommentRequest) {
        val member = memberService.getById(customUserDetails.getId())
        val letter = letterService.getById(letterId)
        commentService.upsert(
            id = upsertCommentRequest.commentId,
            letterId = letter.id,
            authorId = member.id,
            nickname = member.nickname,
            upsertCommentRequest = upsertCommentRequest
        )
    }

    @Transactional
    fun delete(customUserDetails: CustomUserDetails, letterId: Long, commentId: Long) {
        val member = memberService.getById(customUserDetails.getId())
        val comment = commentService.getById(commentId)
        if (comment.authorId != member.id) {
            throw BadRequestException("sarangggun.comment.unauthorized")
        }
        if (comment.letterId != letterId) {
            throw BadRequestException("sarangggun.comment.invalid-letter")
        }
        commentService.delete(commentId)
    }
}
