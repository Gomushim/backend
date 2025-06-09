package gomushin.backend.schedule.domain.service

import gomushin.backend.core.common.support.SpringContextHolder
import gomushin.backend.core.configuration.env.AppEnv
import gomushin.backend.core.infrastructure.exception.BadRequestException
import gomushin.backend.schedule.domain.entity.Comment
import gomushin.backend.schedule.domain.repository.CommentRepository
import gomushin.backend.schedule.dto.request.UpsertCommentRequest
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
import org.springframework.data.repository.findByIdOrNull
import kotlin.test.Test

@ExtendWith(MockKExtension::class)
class CommentServiceTest {

    @MockK
    private lateinit var commentRepository: CommentRepository

    @MockK(relaxed = true)
    private lateinit var mockAppEnv: AppEnv

    @MockK
    private lateinit var mockApplicationContext: ApplicationContext

    @InjectMockKs
    private lateinit var commentService: CommentService

    @BeforeEach
    fun setup() {
        SpringContextHolder.context = mockApplicationContext
        every { mockApplicationContext.getBean(AppEnv::class.java) } returns mockAppEnv
        every { mockAppEnv.getId() } returns "test-env"
    }

    @Nested
    inner class UpsertTest {
        @DisplayName("id가 입력으로 들어오지 않는 경우, 댓글을 생성한다.")
        @Test
        fun upsert_shouldCreateComment_When_ParameterId_NotExists() {
            // given
            val letterId = 1L
            val authorId = 1L
            val nickname = "닉네임"
            val upsertCommentRequest = mockk<UpsertCommentRequest>()
            val comment = mockk<Comment>()
            every { upsertCommentRequest.content } returns "훈련 힘내"
            every { upsertCommentRequest.commentId } returns 1L
            every { commentRepository.save(any()) } returns comment

            // when
            commentService.upsert(null, letterId, authorId, nickname, upsertCommentRequest)

            // then
            verify { commentService.save(any()) }
        }

        @DisplayName("id로 찾은 댓글의 작성자와 수정하려는 authorId가 다른 경우, 에러를 반환한다.")
        @Test
        fun upsert_shouldThrowException_When_CommentAuthorId_And_AuthorId_Not_Matched() {
            // given
            val id = 1L
            val letterId = 1L
            val authorId = 1L
            val nickname = "닉네임"
            val upsertCommentRequest = mockk<UpsertCommentRequest>()
            val comment = mockk<Comment>()
            every { commentRepository.findByIdOrNull(1L) } returns comment
            every { upsertCommentRequest.content } returns "훈련 힘내"
            every { upsertCommentRequest.commentId } returns 1L
            every { comment.authorId } returns 2L

            // when
            val exception = shouldThrow<BadRequestException> {
                commentService.upsert(id, letterId, authorId, nickname, upsertCommentRequest)
            }

            // then
            exception.error.element.message.resolved shouldBe "댓글은 작성자만 삭제하거나 업데이트 할 수 있어요."
        }
    }

    @Nested
    inner class ReadTest {
        @DisplayName("존재하지 않는 댓글 ID로 조회 시 BadRequestException 발생")
        @Test
        fun getById_shouldThrowBadRequestException_When_NotExistId() {
            every { commentRepository.findByIdOrNull(any()) } returns null

            val exception = shouldThrow<BadRequestException> {
                commentService.getById(999L)
            }

            exception.error.element.message.resolved shouldBe "댓글을 찾을 수 없어요."
        }

        @DisplayName("존재하는 댓글 ID로 조회 시 댓글 객체 반환")
        @Test
        fun getById_success() {
            // given
            val id = 1L
            val comment = mockk<Comment>()
            every { commentRepository.findByIdOrNull(id) } returns comment
            // when
            val result = commentService.getById(id)

            // then
            result shouldBe comment
            verify { commentRepository.findByIdOrNull(id) }
            verify { commentService.findById(id) }
        }

        @DisplayName("findById 호출 시 존재하지 않는 ID로 조회 시 null 반환")
        @Test
        fun findById_shouldReturnNull_When_NotExistId() {
            // given
            val id = 999L
            every { commentRepository.findByIdOrNull(id) } returns null

            // when
            val result = commentService.findById(id)

            // then
            result shouldBe null
            verify { commentRepository.findByIdOrNull(id) }
        }

        @DisplayName("findById 호출 시 존재하는 ID로 조회 시 댓글 객체 반환")
        @Test
        fun findById_shouldReturnComment_When_ExistId() {
            // given
            val id = 1L
            val comment = mockk<Comment>()
            every { commentRepository.findByIdOrNull(id) } returns comment

            // when
            val result = commentService.findById(id)

            // then
            result shouldBe comment
            verify { commentRepository.findByIdOrNull(id) }
        }

        @DisplayName("findAllByLetterId 호출 시 댓글 리스트 반환")
        @Test
        fun findAllByLetterId_success() {
            // given
            val letterId = 1L
            val comments = listOf(mockk<Comment>(), mockk<Comment>())
            every { commentRepository.findAllByLetterId(letterId) } returns comments

            // when
            val result = commentService.findAllByLetterId(letterId)

            // then
            result shouldBe comments
            verify { commentRepository.findAllByLetterId(letterId) }
        }
    }

    @DisplayName("save 호출 시 댓글 저장 후 반환")
    @Test
    fun save_shouldReturnSavedComment() {
        // given
        val comment = mockk<Comment>()
        every { commentRepository.save(comment) } returns comment
        // when
        val result = commentService.save(comment)

        // then
        result shouldBe comment
        verify { commentRepository.save(comment) }
    }

    @Nested
    inner class DeleteTest {
        @DisplayName("delete 호출 시 댓글 삭제")
        @Test
        fun delete_shouldDeleteComment() {
            // given
            val id = 1L
            every { commentRepository.deleteById(id) } returns Unit

            // when
            commentService.delete(id)

            // then
            verify { commentRepository.deleteById(id) }
        }

        @DisplayName("deleteAllByLetterId 호출 시 commentRepository.deleteAllByLetterId 가 1회 호출된다.")
        @Test
        fun deleteAllByLetterId_shouldCallDeleteAllByLetterId() {
            // given
            val letterId = 1L
            every { commentRepository.deleteAllByLetterId(any()) } returns Unit

            // when
            commentService.deleteAllByLetterId(letterId)

            // then
            verify { commentRepository.deleteAllByLetterId(letterId) }
        }
    }

}
