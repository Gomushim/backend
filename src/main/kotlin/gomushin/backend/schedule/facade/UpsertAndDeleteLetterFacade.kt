package gomushin.backend.schedule.facade

import gomushin.backend.core.CustomUserDetails
import gomushin.backend.core.infrastructure.exception.BadRequestException
import gomushin.backend.core.service.S3Service
import gomushin.backend.schedule.domain.service.LetterService
import gomushin.backend.schedule.domain.service.PictureService
import gomushin.backend.schedule.domain.service.ScheduleService
import gomushin.backend.schedule.dto.request.UpsertLetterRequest
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Component
class UpsertAndDeleteLetterFacade(
    private val letterService: LetterService,
    private val s3Service: S3Service,
    private val pictureService: PictureService,
    private val scheduleService: ScheduleService,
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

        val pictureList = mutableListOf<String>()

        pictures?.let {
            it.forEach { picture ->
                val fileUrl = s3Service.uploadFile(picture)
                pictureList.add(fileUrl)
            }
            pictureService.upsert(letter.id, pictureList)
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
