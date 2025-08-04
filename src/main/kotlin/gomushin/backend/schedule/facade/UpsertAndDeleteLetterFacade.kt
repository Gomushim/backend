package gomushin.backend.schedule.facade

import gomushin.backend.core.CustomUserDetails
import gomushin.backend.core.event.dto.S3DeleteEvent
import gomushin.backend.core.infrastructure.exception.BadRequestException
import gomushin.backend.core.service.S3Service
import gomushin.backend.schedule.domain.entity.Picture
import gomushin.backend.schedule.domain.service.LetterService
import gomushin.backend.schedule.domain.service.PictureService
import gomushin.backend.schedule.domain.service.ScheduleService
import gomushin.backend.schedule.dto.request.UpsertLetterRequest
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Component
class UpsertAndDeleteLetterFacade(
    private val letterService: LetterService,
    private val s3Service: S3Service,
    private val pictureService: PictureService,
    private val scheduleService: ScheduleService,
    private val applicationEventPublisher: ApplicationEventPublisher,
) {
    // TODO: 이벤트 드리븐하게 수정
    @Transactional
    fun upsert(
        customUserDetails: CustomUserDetails,
        upsertLetterRequest: UpsertLetterRequest,
        pictures: List<MultipartFile>?
    ) {

        val schedule = scheduleService.getById(upsertLetterRequest.scheduleId)

        if (schedule.coupleId != customUserDetails.getCouple().id) {
            throw BadRequestException("sarangggun.letter.not-in-couple")
        }

        val letter = letterService.upsert(
            customUserDetails.getId(),
            customUserDetails.username,
            customUserDetails.getCouple(),
            upsertLetterRequest
        )

        val existingPictures = pictureService.findAllByLetterId(letter.id)
        val existingUrls = existingPictures.map { it.pictureUrl }

        val toDelete = existingPictures.filter { it.pictureUrl !in upsertLetterRequest.pictureUrls }
        val pictureUrlsToDelete = toDelete.map { it.pictureUrl }
        pictureService.deleteAll(toDelete)

        val uploadedUrls = pictures?.map { s3Service.uploadFile(it) } ?: emptyList()
        uploadedUrls
            .filter { it !in existingUrls }
            .forEach { url ->
                pictureService.saveAll(listOf(Picture(letterId = letter.id, pictureUrl = url)))
            }

        if (pictureUrlsToDelete.isNotEmpty()) {
            applicationEventPublisher.publishEvent(S3DeleteEvent(pictureUrlsToDelete))
        }
    }

    @Transactional
    fun delete(
        customUserDetails: CustomUserDetails,
        scheduleId: Long,
        letterId: Long
    ) {
        val letter = letterService.getById(letterId)

        if (letter.authorId != customUserDetails.getId()) {
            throw BadRequestException("sarangggun.letter.unauthorized")
        }

        if (letter.scheduleId != scheduleId) {
            throw BadRequestException("sarangggun.letter.invalid-schedule")
        }

        pictureService.findAllByLetter(letter)
            .takeIf { it.isNotEmpty() }
            ?.forEach { picture ->
                s3Service.deleteFile(picture.pictureUrl)
            }

        letterService.delete(letterId)
        pictureService.deleteAllByLetterId(letterId)
    }
}
