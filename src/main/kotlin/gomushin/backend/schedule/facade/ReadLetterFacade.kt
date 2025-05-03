package gomushin.backend.schedule.facade

import gomushin.backend.core.CustomUserDetails
import gomushin.backend.core.common.web.PageResponse
import gomushin.backend.schedule.domain.service.CommentService
import gomushin.backend.schedule.domain.service.LetterService
import gomushin.backend.schedule.domain.service.PictureService
import gomushin.backend.schedule.domain.service.ScheduleService
import gomushin.backend.schedule.dto.request.ReadLettersToMePaginationRequest
import gomushin.backend.schedule.dto.response.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class ReadLetterFacade(
    private val letterService: LetterService,
    private val scheduleService: ScheduleService,
    private val pictureService: PictureService,
    private val commentService: CommentService,
    @Value("\${server.url}")
    private val baseUrl: String,
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

    fun getLetterListToMe(
        customUserDetails: CustomUserDetails,
        readLettersToMePaginationRequest: ReadLettersToMePaginationRequest,
    ): PageResponse<LetterPreviewResponse> {

        val partnerPk = if (customUserDetails.getCouple().invitorId == customUserDetails.getId()) {
            customUserDetails.getCouple().inviteeId
        } else {
            customUserDetails.getCouple().invitorId
        }

        val letters = letterService.findArrivedToMe(customUserDetails.getCouple(), partnerPk, readLettersToMePaginationRequest)

        val letterPreviewResponses = letters.map { letter ->
            val picture = letter?.let { pictureService.findFirstByLetterId(it.id) }
            LetterPreviewResponse.of(letter, picture)
        }

        val isLastPage = letterPreviewResponses.size < readLettersToMePaginationRequest.take

        val hasData = letterPreviewResponses.isNotEmpty()

//        val nextUrl = if (!isLastPage && hasData) {
//            "${baseUrl}/v1/schedules/letters/to-me?key=${letterPreviewResponses.last().letterId}&orderCreatedAt=DESC&take=${readLettersToMePaginationRequest.take}"
//        } else {
//            null
//        }

        return PageResponse.of(
            data = letterPreviewResponses,
            after = if (hasData) letterPreviewResponses.last().letterId else null,
            count = letterPreviewResponses.size,
//            next = nextUrl,
            isLastPage = isLastPage
        )
    }

    fun getLetterListMain(
        customUserDetails: CustomUserDetails,
    ): List<MainLetterPreviewResponse> {
        val letters = letterService.findTop5ByCreatedDateDesc(customUserDetails.getCouple())
        return letters.map { letter ->
            val picture = pictureService.findFirstByLetterId(letter.id)
            val schedule = scheduleService.findById(letter.scheduleId)
            MainLetterPreviewResponse.of(letter, picture, schedule)
        }
    }
}
