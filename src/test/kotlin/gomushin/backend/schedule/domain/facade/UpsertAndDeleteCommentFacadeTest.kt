package gomushin.backend.schedule.domain.facade

import gomushin.backend.core.CustomUserDetails
import gomushin.backend.core.common.support.SpringContextHolder
import gomushin.backend.core.configuration.env.AppEnv
import gomushin.backend.core.infrastructure.exception.BadRequestException
import gomushin.backend.member.domain.entity.Member
import gomushin.backend.member.domain.service.MemberService
import gomushin.backend.schedule.domain.entity.Comment
import gomushin.backend.schedule.domain.entity.Letter
import gomushin.backend.schedule.domain.service.CommentService
import gomushin.backend.schedule.domain.service.LetterService
import gomushin.backend.schedule.dto.request.UpsertCommentRequest
import gomushin.backend.schedule.facade.UpsertAndDeleteCommentFacade
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.context.ApplicationContext
import kotlin.test.Test

@ExtendWith(MockKExtension::class)
class UpsertAndDeleteCommentFacadeTest {

    @MockK
    private lateinit var commentService: CommentService

    @MockK
    private lateinit var letterService: LetterService

    @MockK
    private lateinit var memberService: MemberService

    @MockK(relaxed = true)
    private lateinit var mockAppEnv: AppEnv

    @MockK
    private lateinit var mockApplicationContext: ApplicationContext

    @InjectMockKs
    private lateinit var upsertAndDeleteCommentFacade: UpsertAndDeleteCommentFacade

    @BeforeEach
    fun setup() {
        SpringContextHolder.context = mockApplicationContext
        every { mockApplicationContext.getBean(AppEnv::class.java) } returns mockAppEnv
        every { mockAppEnv.getId() } returns "test-env"
    }


    @DisplayName("댓글 생성 또는 수정 성공")
    @Test
    fun upsert_success() {
        // given
        val customUserDetails = mockk<CustomUserDetails>()
        val letterId = 1L
        val upsertCommentRequest = mockk<UpsertCommentRequest>()
        val member = mockk<Member>()
        val letter = mockk<Letter>()
        every { customUserDetails.getId() } returns 1L
        every { memberService.getById(any()) } returns member
        every { letterService.getById(any()) } returns letter
        every { upsertCommentRequest.commentId } returns 1L
        every { letter.id } returns letterId
        every { member.id } returns 1L
        every { member.nickname } returns "닉네임"
        every { commentService.upsert(any(), any(), any(), any(), any()) } returns Unit

        // when
        upsertAndDeleteCommentFacade.upsert(customUserDetails, letterId, upsertCommentRequest)

        // then
        verify { memberService.getById(1L) }
        verify { letterService.getById(1L) }
        verify { commentService.upsert(1L, 1L, 1L, "닉네임", upsertCommentRequest) }
    }


    @Nested
    inner class DeleteTest {
        @DisplayName("댓글 삭제 성공")
        @Test
        fun delete_success() {
            // given
            val customUserDetails = mockk<CustomUserDetails>()
            val letterId = 1L
            val memberId = 1L
            val commentId = 1L
            val authorId = 1L
            val member = mockk<Member>()
            val comment = mockk<Comment>()

            every { customUserDetails.getId() } returns 1L
            every { memberService.getById(any()) } returns member
            every { commentService.getById(commentId) } returns comment
            every { comment.authorId } returns authorId
            every { member.id } returns memberId
            every { comment.letterId } returns letterId
            every { commentService.delete(commentId) } returns Unit

            // when
            upsertAndDeleteCommentFacade.delete(customUserDetails, letterId, commentId)

            // then
            verify { memberService.getById(1L) }
            verify { commentService.getById(commentId) }
            verify { commentService.delete(commentId) }
        }

        @DisplayName("댓글 삭제 시 comment 작성자 ID와 Member ID가 다를 경우, 에러 발생")
        @Test
        fun delete_shouldThrowBadRequestException_When_Comment_AuthorId_is_Not_MemberId() {
            // given
            val customUserDetails = mockk<CustomUserDetails>()
            val letterId = 1L
            val memberId = 2L
            val commentId = 1L
            val authorId = 1L
            val member = mockk<Member>()
            val comment = mockk<Comment>()

            every { customUserDetails.getId() } returns 1L
            every { memberService.getById(any()) } returns member
            every { commentService.getById(commentId) } returns comment
            every { comment.authorId } returns authorId
            every { member.id } returns memberId
            every { comment.letterId } returns letterId
            every { commentService.delete(commentId) } returns Unit

            // when
            val exception = shouldThrow<BadRequestException> {
                upsertAndDeleteCommentFacade.delete(customUserDetails, letterId, commentId)
            }

            // then
            exception.error.element.message.resolved shouldBe "댓글은 작성자만 삭제하거나 업데이트 할 수 있어요."
        }

        @DisplayName("댓글의 letterId와 입력으로 받은 letterId가 다른 경우, 예외를 발생시킨다.")
        @Test
        fun delete_shouldThrowBadRequestException_when_commentLetterId_and_letterId_not_match() {
            // given
            val customUserDetails = mockk<CustomUserDetails>()
            val letterId = 1L
            val memberId = 1L
            val commentId = 1L
            val authorId = 1L
            val member = mockk<Member>()
            val comment = mockk<Comment>()

            every { customUserDetails.getId() } returns 1L
            every { memberService.getById(any()) } returns member
            every { commentService.getById(commentId) } returns comment
            every { comment.authorId } returns authorId
            every { member.id } returns memberId
            every { comment.letterId } returns 2L
            every { commentService.delete(commentId) } returns Unit

            // when
            val exception = shouldThrow<BadRequestException> {
                upsertAndDeleteCommentFacade.delete(customUserDetails, letterId, commentId)
            }

            // then
            exception.error.element.message.resolved shouldBe "편지에 해당하는 댓글이 아니에요."
        }
    }
}
