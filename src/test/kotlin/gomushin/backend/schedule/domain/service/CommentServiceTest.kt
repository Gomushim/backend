package gomushin.backend.schedule.domain.service

import gomushin.backend.schedule.domain.entity.Comment
import gomushin.backend.schedule.domain.repository.CommentRepository
import gomushin.backend.schedule.dto.request.UpsertCommentRequest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*
import kotlin.test.Test

@ExtendWith(MockitoExtension::class)
class CommentServiceTest {

    @Mock
    lateinit var commentRepository: CommentRepository

    @InjectMocks
    lateinit var commentService: CommentService

    @Nested
    inner class Upsert {

        @DisplayName("업데이트 성공 - 댓글이 존재할 때")
        @Test
        fun upload_success() {
            // given
            val id = 1L
            val letterId = 1L
            val authorId = 1L
            val nickname = "닉네임"
            val upsertCommentRequest = UpsertCommentRequest(
                commentId = 1L,
                content = "내용"
            )
            val mockComment = mock(Comment::class.java).apply {
                `when`(this.authorId).thenReturn(authorId)
            }

            // when
            `when`(commentRepository.findById(id)).thenReturn(Optional.of(mockComment))
            commentService.upsert(id, letterId, authorId, nickname, upsertCommentRequest)

            // then
            verify(mockComment).content = upsertCommentRequest.content
        }

        @DisplayName("댓글 생성 성공")
        @Test
        fun insert_success() {
            // given
            val letterId = 1L
            val authorId = 1L
            val nickname = "닉네임"
            val upsertCommentRequest = UpsertCommentRequest(
                commentId = null,
                content = "내용"
            )
            val mockComment = mock(Comment::class.java)

            // when
            `when`(commentRepository.save(any())).thenReturn(mockComment)
            commentService.upsert(null, letterId, authorId, nickname, upsertCommentRequest)

            // then
            verify(commentRepository, times(1)).save(org.mockito.kotlin.any())
        }

    }
}
