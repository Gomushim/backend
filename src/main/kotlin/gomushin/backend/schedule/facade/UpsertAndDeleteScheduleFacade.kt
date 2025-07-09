package gomushin.backend.schedule.facade

import gomushin.backend.core.CustomUserDetails
import gomushin.backend.core.event.dto.S3DeleteEvent
import gomushin.backend.schedule.domain.service.CommentService
import gomushin.backend.schedule.domain.service.LetterService
import gomushin.backend.schedule.domain.service.PictureService
import gomushin.backend.schedule.domain.service.ScheduleService
import gomushin.backend.schedule.dto.request.UpsertScheduleRequest
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class UpsertAndDeleteScheduleFacade(
    private val scheduleService: ScheduleService,
    private val letterService: LetterService,
    private val commentService: CommentService,
    private val pictureService: PictureService,
    private val applicationEventPublisher: ApplicationEventPublisher,
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
        val pictureUrlsToDelete = mutableListOf<String>()
        letterService.findByCoupleAndSchedule(customUserDetails.getCouple(), schedule).forEach { letter ->
            pictureService.findAllByLetter(letter)
                .takeIf { it.isNotEmpty() }
                ?.forEach { picture ->
                    pictureUrlsToDelete.add(picture.pictureUrl)
                }
            pictureService.deleteAllByLetterId(letter.id)
            commentService.deleteAllByLetterId(letter.id)
            letterService.delete(letter.id)
        }
        scheduleService.delete(customUserDetails.getCouple().id, customUserDetails.getId(), scheduleId)

        if (pictureUrlsToDelete.isNotEmpty()) {
            applicationEventPublisher.publishEvent(
                S3DeleteEvent(
                    pictureUrls = pictureUrlsToDelete
                )
            )
        }
    }
}
