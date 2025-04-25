package gomushin.backend.schedule.domain.repository

import gomushin.backend.schedule.domain.entity.Comment
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository: JpaRepository<Comment, Long>
