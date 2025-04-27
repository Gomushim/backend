package gomushin.backend.schedule.domain.repository

import gomushin.backend.schedule.domain.entity.Schedule
import gomushin.backend.schedule.dto.response.DailyScheduleResponse
import gomushin.backend.schedule.dto.response.MonthlySchedulesResponse
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate

interface ScheduleRepository : JpaRepository<Schedule, Long> {
    @Modifying
    @Query("DELETE FROM Schedule s WHERE s.userId = :userId")
    fun deleteAllByUserId(@Param("userId") userId: Long)

    @Query(
        """
        SELECT new gomushin.backend.schedule.dto.response.MonthlySchedulesResponse(
            s.title, 
            s.startDate, 
            s.endDate, 
            s.fatigue
        )
        FROM Schedule s
        WHERE s.coupleId = :coupleId 
        AND function('YEAR', s.startDate) = :year 
        AND function('MONTH', s.startDate) = :month
    """
    )
    fun findByCoupleIdAndYearAndMonth(coupleId: Long, year: Int, month: Int): List<MonthlySchedulesResponse>

    @Query(
        """
        SELECT new gomushin.backend.schedule.dto.response.DailyScheduleResponse(
            s.id, 
            s.title, 
            s.fatigue, 
            s.startDate, 
            s.endDate, 
            s.isAllDay
        )
        FROM Schedule s
        WHERE s.coupleId = :coupleId 
        AND function('DATE', s.startDate) = :startDate
    """
    )
    fun findByCoupleIdAndStartDate(coupleId: Long, startDate: LocalDate): List<DailyScheduleResponse>
}
