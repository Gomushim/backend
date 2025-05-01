package gomushin.backend.couple.domain.repository

import gomushin.backend.couple.domain.entity.Anniversary
import gomushin.backend.couple.dto.response.MonthlyAnniversariesResponse
import gomushin.backend.schedule.dto.response.DailyAnniversaryResponse
import gomushin.backend.schedule.dto.response.MainAnniversariesResponse
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate
import java.time.LocalDateTime

interface AnniversaryRepository : JpaRepository<Anniversary, Long> {
    @Modifying
    @Query(
        """
    DELETE FROM Anniversary a
    WHERE (a.title LIKE '%일' OR a.title LIKE '%주년')
    AND a.anniversaryProperty = 0
    And a.coupleId = :coupleId
"""
    )
    fun deleteAnniversariesWithTitleEndingAndPropertyZero(@Param("coupleId") coupleId: Long)

    @Modifying
    @Query("DELETE FROM Anniversary a WHERE a.coupleId = :coupleId")
    fun deleteAllByCoupleId(@Param("coupleId") coupleId: Long)

    @Query(
        """
        SELECT new gomushin.backend.couple.dto.response.MonthlyAnniversariesResponse(
            a.title, 
            a.anniversaryDate
        )
        FROM Anniversary a
        WHERE a.coupleId = :coupleId 
        AND function('YEAR', a.anniversaryDate) = :year 
        AND function('MONTH', a.anniversaryDate) = :month
    """
    )
    fun findByCoupleIdAndYearAndMonth(coupleId: Long, year: Int, month: Int): List<MonthlyAnniversariesResponse>

    @Query(
        """
        SELECT new gomushin.backend.schedule.dto.response.DailyAnniversaryResponse(
            a.id, 
            a.title, 
            a.anniversaryDate
        )
        FROM Anniversary a
        WHERE a.coupleId = :coupleId 
        AND function('DATE', a.anniversaryDate) = :startDate
    """
    )
    fun findByCoupleIdAndDate(coupleId: Long, startDate: LocalDate): List<DailyAnniversaryResponse>


    @Query(
        """
        SELECT new gomushin.backend.schedule.dto.response.MainAnniversariesResponse(
            a.id, 
            a.title, 
            a.anniversaryDate
        )
        FROM Anniversary a
        WHERE a.coupleId = :coupleId 
        AND a.anniversaryDate BETWEEN :startDate AND :endDate
    """
    )
    fun findByCoupleIdAndDateBetween(
        coupleId: Long,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<MainAnniversariesResponse>


    @Query(
        value =
        """
            SELECT * FROM anniversary
            WHERE couple_id = :coupleId
                AND anniversary_date > CURRENT_DATE
            ORDER BY anniversary_date ASC
            LIMIT 3
        """,
        nativeQuery = true
    )
    fun findTop3UpcomingAnniversaries(
        @Param("coupleId") coupleId: Long
    ): List<Anniversary>


    @Query(
        value =
        """
            SELECT * 
            FROM anniversary a
            WHERE a.couple_id = :coupleId
                AND a.id < :key
            ORDER BY anniversary_date DESC
            LIMIT :take
        """,
        nativeQuery = true
    )
    fun findAnniversaries(
        @Param("coupleId") coupleId: Long,
        @Param("key") key: Long,
        @Param("take") take: Long
    ): List<Anniversary?>
}
