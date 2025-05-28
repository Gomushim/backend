package gomushin.backend.schedule.domain.service

import gomushin.backend.core.common.support.SpringContextHolder
import gomushin.backend.core.configuration.env.AppEnv
import gomushin.backend.core.infrastructure.exception.BadRequestException
import gomushin.backend.schedule.domain.entity.Comment
import gomushin.backend.schedule.domain.entity.Letter
import gomushin.backend.schedule.domain.repository.CommentRepository
import gomushin.backend.schedule.dto.request.UpsertCommentRequest
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
import org.springframework.data.repository.findByIdOrNull
import kotlin.test.Test
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
class CommentServiceTest {

    @MockK
    lateinit var commentRepository: CommentRepository

    @InjectMockKs
    private lateinit var commentService: CommentService

    @BeforeEach
    fun setUp() {
        mockkObject(SpringContextHolder)
        val mockAppEnv = mockk<AppEnv>()
        every { SpringContextHolder.getBean(AppEnv::class.java) } returns mockAppEnv
        every { mockAppEnv.getId() } returns "test"
    }

    @DisplayName("업로드 성공 - 댓글이 존재하지 않을 때")
    @Test
    fun upload_success() {
        // given
        val id = null
        val letterId = 1L
        val authorId = 1L
        val nickname = "작성자"
        val upsertCommentRequest = mockk<UpsertCommentRequest>()
        every { upsertCommentRequest.content } returns "댓글 내용"
        every { commentRepository.save(any<Comment>()) } returns mockk<Comment>()

        // when
        commentService.upsert(
            id = id,
            letterId = letterId,
            authorId = authorId,
            nickname = nickname,
            upsertCommentRequest = upsertCommentRequest
        )

        // then
        verify { commentRepository.save(any<Comment>()) }
    }

    @DisplayName("업데이트 성공 - 댓글이 존재할 때")
    @Test
    fun update_success() {
        // given
        val id = 1L
        val comment = mockk<Comment>(relaxUnitFun = true) {
            every { this@mockk.id } returns id
            every { authorId } returns 1L
            every { content } returns "기존 내용"
        }
        every { commentRepository.findByIdOrNull(id) } returns comment
        every { commentService.getById(id) } returns comment

        // when
        commentService.upsert(
            id = id,
            letterId = 1L,
            authorId = 1L,
            nickname = "작성자",
            upsertCommentRequest = mockk { every { content } returns "새 내용" }
        )

        // then
        verify { comment.content = "새 내용" }
    }

    @DisplayName("업데이트 실패 - 작성자가 일치하지 않을 때")
    @Test
    fun update_failed_authorId_not_match() {
        // given
        val id = 1L
        val comment = mockk<Comment>(relaxUnitFun = true) {
            every { this@mockk.id } returns id
            every { authorId } returns 1L
            every { content } returns "기존 내용"
        }
        every { commentRepository.findByIdOrNull(id) } returns comment
        every { commentService.getById(id) } returns comment

        // when
        val exception = assertThrows<BadRequestException> {
            commentService.upsert(
                id = id,
                letterId = 1L,
                authorId = 2L,
                nickname = "작성자",
                upsertCommentRequest = mockk { every { content } returns "새 내용" }
            )
        }
        val errorMessage = exception.error.element.message.resolved

        // then
        assertEquals("댓글은 작성자만 삭제하거나 업데이트 할 수 있어요.", errorMessage)

    }

    @DisplayName("성공 - findByIdOrNull 반환이 Null이 아닌 경우")
    @Test
    fun findByIdOrNull_success() {
        // given
        val id = 1L
        val mockComment = mockk<Comment>()
        every { commentRepository.findByIdOrNull(id) } returns mockComment

        // when
        commentService.findById(id)

        // then
        verify { commentRepository.findByIdOrNull(id) }
    }

    @DisplayName("성공 - findByIdOrNull 반환이 Null인 경우")
    @Test
    fun findByIdOrNull_notFound() {
        // given
        val id = 1L
        every { commentRepository.findByIdOrNull(id) } returns null

        // when & then
        commentService.findById(id)

        verify { commentRepository.findByIdOrNull(id) }

    }

    @DisplayName("성공 - getById")
    @Test
    fun getById_success() {
        // given
        val id = 1L
        val mockComment = mockk<Comment>()
        every { commentRepository.findByIdOrNull(id) } returns mockComment

        // when
        commentService.getById(id)

        // then
        verify { commentRepository.findByIdOrNull(id) }
    }

    @DisplayName("실패 - getById - 존재하지 않는 id로 조회 시 BadRequestException 발생")
    @Test
    fun getById_notFound() {
        // given
        val id = 1L
        every { commentRepository.findByIdOrNull(id) } returns null

        // when & then
        val exception = assertThrows<BadRequestException> {
            commentService.getById(id)
        }
        val errorMessage = exception.error.element.message.resolved

        assertEquals("댓글을 찾을 수 없어요.", errorMessage)
    }

    @DisplayName("성공 - findAllByLetter")
    @Test
    fun findAllByLetter_success() {
        // given
        val letterId = 1L
        val mockLetter = mockk<Letter>()
        every { mockLetter.id } returns letterId
        val mockCommentList = listOf(mockk<Comment>())

        every { commentRepository.findAllByLetterId(letterId) } returns mockCommentList

        // when
        commentService.findAllByLetter(mockLetter)

        // then
        verify { commentRepository.findAllByLetterId(letterId) }
    }

    @DisplayName("성공 - delete")
    @Test
    fun delete_success() {
        // given
        val id = 1L

        every { commentRepository.deleteById(id) } returns Unit

        // when
        commentService.delete(id)

        // then
        verify { commentRepository.deleteById(id) }
    }

    @DisplayName("성공 - deleteAllByMemberId")
    @Test
    fun deleteAllByMemberId_success() {
        // given
        val memberId = 1L

        every { commentRepository.deleteAllByAuthorId(memberId) } returns Unit

        // when
        commentService.deleteAllByMemberId(memberId)

        // then
        verify { commentRepository.deleteAllByAuthorId(memberId) }
    }

    @DisplayName("성공 - deleteAllByLetterId")
    @Test
    fun deleteAllByLetterId_success() {
        // given
        val letterId = 1L

        every { commentRepository.deleteAllByLetterId(letterId) } returns Unit

        // when
        commentService.deleteAllByLetterId(letterId)

        // then
        verify { commentRepository.deleteAllByLetterId(letterId) }
    }
}
