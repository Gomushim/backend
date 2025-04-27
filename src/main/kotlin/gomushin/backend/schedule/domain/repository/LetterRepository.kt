package gomushin.backend.schedule.domain.repository

import gomushin.backend.schedule.domain.entity.Letter
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface LetterRepository : JpaRepository<Letter, Long> {
    fun findByCoupleIdAndScheduleId(coupleId: Long, scheduleId: Long): List<Letter>

    fun findByCoupleIdAndScheduleIdAndId(coupleId: Long, scheduleId: Long, letterId: Long): Letter?

    @Query("SELECT l.id FROM Letter l WHERE l.authorId = :authorId")
    fun findAllByAuthorId(@Param("authorId") authorId: Long): List<Long>

    @Modifying
    @Query("DELETE FROM Letter l WHERE l.authorId = :authorId")
    fun deleteAllByAuthorId(@Param("authorId") authorId: Long)
}
