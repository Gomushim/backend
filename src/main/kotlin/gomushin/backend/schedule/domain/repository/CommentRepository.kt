package gomushin.backend.schedule.domain.repository

import gomushin.backend.schedule.domain.entity.Comment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface CommentRepository: JpaRepository<Comment, Long> {
    @Modifying
    @Query("DELETE FROM Comment c WHERE c.authorId = :authorId")
    fun deleteAllByAuthorId(@Param("authorId") authorId: Long)
}
