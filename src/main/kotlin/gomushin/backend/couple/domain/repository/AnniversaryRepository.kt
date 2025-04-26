package gomushin.backend.couple.domain.repository

import gomushin.backend.couple.domain.entity.Anniversary
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface AnniversaryRepository : JpaRepository<Anniversary, Long> {
    @Modifying
    @Query("""
    DELETE FROM Anniversary a
    WHERE (a.title LIKE '%일' OR a.title LIKE '%주년')
    AND a.anniversaryProperty = 0
    And a.coupleId = :coupleId
""")
    fun deleteAnniversariesWithTitleEndingAndPropertyZero(@Param("coupleId")coupleId : Long)
}
