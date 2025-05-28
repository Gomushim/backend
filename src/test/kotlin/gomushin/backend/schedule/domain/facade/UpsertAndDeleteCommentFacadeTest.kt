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
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.Test
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
class UpsertAndDeleteCommentFacadeTest {

    @MockK
    private lateinit var commentService: CommentService

    @MockK
    private lateinit var letterService: LetterService

    @MockK
    private lateinit var memberService: MemberService

    @InjectMockKs
    private lateinit var upsertAndDeleteCommentFacade: UpsertAndDeleteCommentFacade

    private val customUserDetails = mockk<CustomUserDetails>()

    @BeforeEach
    fun setUp() {
        mockkObject(SpringContextHolder)
        val mockAppEnv = mockk<AppEnv>()
        every { SpringContextHolder.getBean(AppEnv::class.java) } returns mockAppEnv
        every { mockAppEnv.getId() } returns "test"
    }

    init {
        every { customUserDetails.getId() } returns 1L
    }

    @DisplayName("댓글 생성 또는 수정 성공")
    @Test
    fun upsert_success() {
        // given
        val letterId = 1L
        val letter = mockk<Letter>()
        val memberId = 1L
        val member = mockk<Member>()
        val commentId = 1L
        val upsertCommentRequest = mockk<UpsertCommentRequest>()

        every { upsertCommentRequest.commentId } returns commentId
        every { memberService.getById(memberId) } returns member
        every { letterService.getById(letterId) } returns letter
        every { letter.id } returns letterId
        every { member.id } returns memberId
        every { member.nickname } returns "작성자"
        every {
            commentService.upsert(
                id = eq(commentId),
                letterId = eq(letterId),
                authorId = eq(memberId),
                nickname = eq("작성자"),
                upsertCommentRequest = eq(upsertCommentRequest)
            )
        } returns Unit

        // when
        upsertAndDeleteCommentFacade.upsert(
            customUserDetails,
            letterId,
            upsertCommentRequest
        )

        // then
        verify(exactly = 1) {
            memberService.getById(memberId)
            letterService.getById(letterId)
            commentService.upsert(
                id = eq(commentId),
                letterId = eq(letterId),
                authorId = eq(memberId),
                nickname = eq("작성자"),
                upsertCommentRequest = eq(upsertCommentRequest)
            )
        }
    }

    @DisplayName("delete 성공")
    @Test
    fun delete() {
        // given
        val letterId = 1L
        val commentId = 1L
        val member = mockk<Member>()
        val comment = mockk<Comment>()
        every { memberService.getById(1L) } returns member
        every { commentService.getById(commentId) } returns comment
        every { member.id } returns 1L
        every { comment.authorId } returns 1L
        every { comment.letterId } returns letterId
        every { commentService.delete(commentId) } returns Unit

        // when
        upsertAndDeleteCommentFacade.delete(
            customUserDetails,
            letterId,
            commentId
        )

        // then
        verify(exactly = 1) {
            memberService.getById(1L)
            commentService.getById(commentId)
            commentService.delete(commentId)
        }
    }

    @DisplayName("delete 실패 - 작성자가 아닌 경우")
    @Test
    fun delete_fail_not_author() {
        // given
        val letterId = 1L
        val commentId = 1L
        val member = mockk<Member>()
        val comment = mockk<Comment>()
        every { memberService.getById(1L) } returns member
        every { commentService.getById(commentId) } returns comment
        every { member.id } returns 2L
        every { comment.authorId } returns 1L
        every { comment.letterId } returns letterId

        // when & then
        val exception = assertThrows<BadRequestException> {
            upsertAndDeleteCommentFacade.delete(
                customUserDetails,
                letterId,
                commentId
            )
        }

        val message = exception.error.element.message.resolved

        assertEquals("댓글은 작성자만 삭제하거나 업데이트 할 수 있어요.", message)
    }

    @DisplayName("delete 실패 - letterId가 일치하지 않는 경우")
    @Test
    fun delete_fail_invalid_letter() {
        // given
        val letterId = 1L
        val commentId = 1L
        val member = mockk<Member>()
        val comment = mockk<Comment>()
        every { memberService.getById(1L) } returns member
        every { commentService.getById(commentId) } returns comment
        every { member.id } returns 1L
        every { comment.authorId } returns 1L
        every { comment.letterId } returns 2L

        // when & then
        val exception = assertThrows<BadRequestException> {
            upsertAndDeleteCommentFacade.delete(
                customUserDetails,
                letterId,
                commentId
            )
        }

        val message = exception.error.element.message.resolved

        assertEquals("편지에 해당하는 댓글이 아니에요.", message)
    }


}
