package gomushin.backend.schedule.facade

import gomushin.backend.core.CustomUserDetails
import gomushin.backend.core.service.S3Service
import gomushin.backend.schedule.domain.service.CommentService
import gomushin.backend.schedule.domain.service.LetterService
import gomushin.backend.schedule.domain.service.PictureService
import gomushin.backend.schedule.domain.service.ScheduleService
import gomushin.backend.schedule.dto.request.UpsertScheduleRequest
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class UpsertAndDeleteScheduleFacade(
    private val scheduleService: ScheduleService,
    private val letterService: LetterService,
    private val commentService: CommentService,
    private val pictureService: PictureService,
    private val s3Service: S3Service,
) {

    fun upsert(customUserDetails: CustomUserDetails, upsertScheduleRequest: UpsertScheduleRequest) {
        scheduleService.upsert(
            upsertScheduleRequest.id,
            customUserDetails.getCouple().id,
            customUserDetails.getId(),
            upsertScheduleRequest
        )
    }

    @Transactional
    fun delete(customUserDetails: CustomUserDetails, scheduleId: Long) {
        val schedule = scheduleService.getById(scheduleId)
        letterService.findByCoupleAndSchedule(customUserDetails.getCouple(), schedule).forEach { letter ->
            pictureService.findAllByLetter(letter)
                .takeIf { it.isNotEmpty() }
                ?.forEach { picture ->
                    s3Service.deleteFile(picture.pictureUrl)
                }
            pictureService.deleteAllByLetterId(letter.id)
            commentService.deleteAllByLetterId(letter.id)
            letterService.delete(letter.id)
        }
        scheduleService.delete(customUserDetails.getCouple().id, customUserDetails.getId(), scheduleId)
    }
}
