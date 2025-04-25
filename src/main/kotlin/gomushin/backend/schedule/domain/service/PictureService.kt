package gomushin.backend.schedule.domain.service

import gomushin.backend.schedule.domain.entity.Picture
import gomushin.backend.schedule.domain.repository.PictureRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PictureService(
    private val pictureRepository: PictureRepository,
) {
    @Transactional
    fun upsert(letterId: Long, pictureUrls: List<String>) {
        deleteAllByLetterId(letterId)
        val newPictures = pictureUrls.map { Picture(letterId = letterId, pictureUrl = it) }
        saveAll(newPictures)
    }

    @Transactional(readOnly = true)
    fun findAll(urls: List<String>): List<Picture> {
        return pictureRepository.findAllByPictureUrlIn(urls)
    }

    @Transactional
    fun saveAll(pictures: List<Picture>): List<Picture> {
        return pictureRepository.saveAll(pictures)
    }

    @Transactional
    fun deleteAllByLetterId(letterId: Long) {
        pictureRepository.deleteAllByLetterId(letterId)
    }
}
