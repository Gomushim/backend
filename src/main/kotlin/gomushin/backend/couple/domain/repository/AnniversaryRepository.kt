package gomushin.backend.couple.domain.repository

import gomushin.backend.couple.domain.entity.Anniversary
import gomushin.backend.couple.dto.response.AnniversaryNotificationInfo
import gomushin.backend.couple.dto.response.MonthlyAnniversariesResponse
import gomushin.backend.schedule.dto.response.DailyAnniversaryResponse
import gomushin.backend.schedule.dto.response.MainAnniversariesResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate

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
            a.emoji,
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
        """
            SELECT a FROM Anniversary a
            WHERE a.coupleId = :coupleId
            ORDER BY a.anniversaryDate DESC
        """
    )
    fun findAnniversaries(
        @Param("coupleId") coupleId: Long,
        pageable: Pageable
    ): Page<Anniversary>

    @Query(
        """
    SELECT a.title AS title, m.id AS memberId, m.fcm_token AS fcmToken
    FROM anniversary a
    JOIN couple c ON a.couple_id = c.id
    JOIN member m ON m.id = c.invitor_id OR m.id = c.invitee_id
    JOIN notification n ON n.member_id = m.id
    WHERE DATE(a.anniversary_date) = :nowDate
      AND n.dday = true
    """,
        nativeQuery = true
    )
    fun findTodayAnniversaryMemberFcmTokens(@Param("nowDate") date: LocalDate): List<AnniversaryNotificationInfo>
}
