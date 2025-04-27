package gomushin.backend.schedule.domain.repository

import gomushin.backend.schedule.domain.entity.Picture
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface PictureRepository : JpaRepository<Picture, Long> {
    fun findAllByPictureUrlIn(urls: List<String>): List<Picture>
    fun deleteAllByLetterId(letterId: Long)
    fun findAllByLetterId(letterId: Long): List<Picture>
    fun findFirstByLetterIdOrderByIdAsc(letterId: Long): Picture?

    @Query("SELECT p FROM Picture p WHERE p.letterId IN :letterIds")
    fun findAllByLetterIdIn(@Param("letterIds") letterIds: List<Long>): List<Picture>

    @Modifying
    @Query("DELETE FROM Picture p WHERE p.letterId IN :letterIds")
    fun deleteAllByLetterIdIn(@Param("letterIds") letterIds: List<Long>)
}
