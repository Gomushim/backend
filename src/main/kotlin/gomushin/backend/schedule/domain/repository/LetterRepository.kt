package gomushin.backend.schedule.domain.repository

import gomushin.backend.schedule.domain.entity.Letter
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface LetterRepository : JpaRepository<Letter, Long> {
    fun findByCoupleId(coupleId: Long): List<Letter>

    fun findByCoupleIdAndScheduleId(coupleId: Long, scheduleId: Long): List<Letter>

    fun findByCoupleIdAndScheduleIdAndId(coupleId: Long, scheduleId: Long, letterId: Long): Letter?

    @Query("SELECT l.id FROM Letter l WHERE l.authorId = :authorId")
    fun findAllByAuthorId(@Param("authorId") authorId: Long): List<Long>

    @Modifying
    @Query("DELETE FROM Letter l WHERE l.authorId = :authorId")
    fun deleteAllByAuthorId(@Param("authorId") authorId: Long)

    @Query(
        """
        SELECT l FROM Letter l
        WHERE l.coupleId = :coupleId
        ORDER BY l.createdAt DESC
        """,
    )
    fun findAllToCouple(
        @Param("coupleId") coupleId: Long,
        pageable: Pageable
    ): Page<Letter>

    fun findTop5ByCoupleIdOrderByCreatedAtDesc(coupleId: Long): List<Letter>
}
