package gomushin.backend.schedule.domain.repository

import gomushin.backend.schedule.domain.entity.Schedule
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ScheduleRepository : JpaRepository<Schedule, Long> {
    @Modifying
    @Query("DELETE FROM Schedule s WHERE s.userId = :userId")
    fun deleteAllByUserId(@Param("userId") userId: Long)
}
