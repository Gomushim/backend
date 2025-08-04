package gomushin.backend.schedule.domain.repository

import gomushin.backend.schedule.domain.entity.Schedule
import gomushin.backend.schedule.dto.response.DailyScheduleResponse
import gomushin.backend.schedule.dto.response.MainSchedulesResponse
import gomushin.backend.schedule.dto.response.MonthlySchedulesResponse
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate
import java.time.LocalDateTime

interface ScheduleRepository : JpaRepository<Schedule, Long> {
    @Modifying
    @Query("DELETE FROM Schedule s WHERE s.userId = :userId")
    fun deleteAllByUserId(@Param("userId") userId: Long)

    @Query(
        """
        SELECT new gomushin.backend.schedule.dto.response.MonthlySchedulesResponse(
            s.id,
            s.title, 
            s.startDate, 
            s.endDate, 
            s.fatigue
        )
        FROM Schedule s
        WHERE s.coupleId = :coupleId 
        AND (
          (function('YEAR', s.startDate) = :year AND function('MONTH', s.startDate) = :month)
          OR 
          (function('YEAR', s.endDate) = :year AND function('MONTH', s.endDate) = :month)
        ) 
        
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
        AND :date BETWEEN FUNCTION('DATE', s.startDate) AND FUNCTION('DATE', s.endDate)
    """
    )
    fun findByCoupleIdAndDate(coupleId: Long, date: LocalDate): List<DailyScheduleResponse>

    @Query(
        """
        SELECT new gomushin.backend.schedule.dto.response.MainSchedulesResponse(
            s.id, 
            s.title,
            s.startDate,
            s.endDate,
            s.fatigue
        )
        FROM Schedule s
        WHERE s.coupleId = :coupleId 
        AND s.startDate <= :endDate 
        AND s.endDate >= :startDate
    """
    )
    fun findByCoupleIdAndStartDateAndEndDateBetween(
        coupleId: Long,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): List<MainSchedulesResponse>
}
