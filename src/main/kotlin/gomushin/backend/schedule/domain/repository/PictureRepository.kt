package gomushin.backend.schedule.domain.repository

import gomushin.backend.schedule.domain.entity.Picture
import org.springframework.data.jpa.repository.JpaRepository

interface PictureRepository: JpaRepository<Picture, Long> {
    fun findAllByPictureUrlIn(urls: List<String>): List<Picture>
    fun deleteAllByLetterId(letterId: Long)
    fun findAllByLetterId(letterId: Long): List<Picture>
}
