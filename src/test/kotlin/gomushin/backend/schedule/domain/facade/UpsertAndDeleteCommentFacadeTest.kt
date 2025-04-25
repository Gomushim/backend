package gomushin.backend.schedule.domain.facade

import gomushin.backend.core.CustomUserDetails
import gomushin.backend.member.domain.entity.Member
import gomushin.backend.member.domain.service.MemberService
import gomushin.backend.schedule.domain.entity.Comment
import gomushin.backend.schedule.domain.entity.Letter
import gomushin.backend.schedule.domain.service.CommentService
import gomushin.backend.schedule.domain.service.LetterService
import gomushin.backend.schedule.dto.UpsertCommentRequest
import gomushin.backend.schedule.facade.UpsertAndDeleteCommentFacade
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import kotlin.test.Test

@ExtendWith(MockitoExtension::class)
class UpsertAndDeleteCommentFacadeTest {

    @Mock
    private lateinit var commentService: CommentService

    @Mock
    private lateinit var letterService: LetterService

    @Mock
    private lateinit var memberService: MemberService

    @InjectMocks
    private lateinit var upsertAndDeleteCommentFacade: UpsertAndDeleteCommentFacade

    @DisplayName("댓글 생성 또는 수정 성공")
    @Test
    fun upsert_success() {
        // given
        val customUserDetails = mock(CustomUserDetails::class.java)
        val letterId = 1L
        val memberId = 1L
        val upsertCommentRequest = UpsertCommentRequest(
            commentId = null,
            content = "댓글 내용"
        )

        val mockMember = mock(Member::class.java).apply {
            `when`(id).thenReturn(memberId)
            `when`(nickname).thenReturn("테스트유저")
        }
        val mockLetter = mock(Letter::class.java).apply {
            `when`(id).thenReturn(letterId)
        }

        `when`(customUserDetails.getId()).thenReturn(memberId)
        `when`(memberService.getById(memberId)).thenReturn(mockMember)
        `when`(letterService.getById(letterId)).thenReturn(mockLetter)

        // when
        upsertAndDeleteCommentFacade.upsert(customUserDetails, letterId, upsertCommentRequest)

        // then
        verify(commentService, times(1)).upsert(
            id = upsertCommentRequest.commentId,
            letterId = mockLetter.id,
            authorId = mockMember.id,
            nickname = "테스트유저",
            upsertCommentRequest = upsertCommentRequest
        )
    }

    @DisplayName("댓글 삭제 성공")
    @Test
    fun delete_success() {
        // given
        val customUserDetails = mock(CustomUserDetails::class.java)
        val letterId = 1L
        val commentId = 1L
        val mockMember = mock(Member::class.java)
        val mockComment = mock(Comment::class.java)

        // when
        `when`(customUserDetails.getId()).thenReturn(1L)
        `when`(memberService.getById(customUserDetails.getId())).thenReturn(mockMember)
        `when`(commentService.getById(commentId)).thenReturn(mockComment)
        `when`(mockMember.id).thenReturn(1L)
        `when`(mockComment.authorId).thenReturn(1L)
        `when`(mockComment.letterId).thenReturn(letterId)
        `when`(mockComment.letterId).thenReturn(letterId)

        upsertAndDeleteCommentFacade.delete(customUserDetails, letterId, commentId)

        // then
        verify(memberService, times(1)).getById(customUserDetails.getId())
        verify(commentService, times(1)).getById(commentId)
        verify(commentService, times(1)).delete(commentId)
    }
}
