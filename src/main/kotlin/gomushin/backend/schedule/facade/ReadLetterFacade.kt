package gomushin.backend.schedule.facade

import gomushin.backend.core.CustomUserDetails
import gomushin.backend.schedule.domain.service.CommentService
import gomushin.backend.schedule.domain.service.LetterService
import gomushin.backend.schedule.domain.service.PictureService
import gomushin.backend.schedule.domain.service.ScheduleService
import gomushin.backend.schedule.dto.response.*
import org.springframework.stereotype.Component

@Component
class ReadLetterFacade(
    private val letterService: LetterService,
    private val scheduleService: ScheduleService,
    private val pictureService: PictureService,
    private val commentService: CommentService,
) {

    fun getList(
        customUserDetails: CustomUserDetails,
        scheduleId: Long,
    ): List<LetterPreviewResponse> {
        val schedule = scheduleService.getById(scheduleId)
        val letters = letterService.findByCoupleAndSchedule(
            customUserDetails.getCouple(),
            schedule,
        )
        return letters.map { letter ->
            val picture = pictureService.findFirstByLetterId(letter.id)
            LetterPreviewResponse.of(letter, picture)
        }
    }

    fun get(
        customUserDetails: CustomUserDetails,
        scheduleId: Long,
        letterId: Long,
    ): LetterDetailResponse {
        val schedule = scheduleService.getById(scheduleId)
        val letter = letterService.getByCoupleAndScheduleAndId(
            customUserDetails.getCouple(),
            schedule,
            letterId,
        )
        val letterResponse = LetterResponse.of(letter)

        val pictures = pictureService.findAllByLetter(letter)
        val pictureResponses = pictures.map { picture ->
            PictureResponse.of(picture)
        }

        val comments = commentService.findAllByLetter(letter)
        val commentResponses = comments.map { comment ->
            CommentResponse.of(comment)
        }

        return LetterDetailResponse.of(
            letter = letterResponse,
            pictures = pictureResponses,
            comments = commentResponses,
        )
    }
}
