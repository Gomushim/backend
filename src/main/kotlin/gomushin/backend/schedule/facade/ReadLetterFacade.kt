package gomushin.backend.schedule.facade

import gomushin.backend.core.CustomUserDetails
import gomushin.backend.core.common.web.PageResponse
import gomushin.backend.member.domain.service.MemberService
import gomushin.backend.schedule.domain.service.CommentService
import gomushin.backend.schedule.domain.service.LetterService
import gomushin.backend.schedule.domain.service.PictureService
import gomushin.backend.schedule.domain.service.ScheduleService
import gomushin.backend.schedule.dto.response.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component

@Component
class ReadLetterFacade(
    private val letterService: LetterService,
    private val scheduleService: ScheduleService,
    private val pictureService: PictureService,
    private val commentService: CommentService,
    private val memberService: MemberService,
    @Value("\${server.url}")
    private val baseUrl: String,
) {


    fun getList(
        customUserDetails: CustomUserDetails,
        scheduleId: Long,
    ): List<LetterPreviewResponse> {
        val member = memberService.getById(customUserDetails.getId())
        val schedule = scheduleService.getById(scheduleId)
        val letters = letterService.findByCoupleAndSchedule(
            customUserDetails.getCouple(),
            schedule,
        )
        return letters.map { letter ->
            val picture = pictureService.findFirstByLetterId(letter.id)
            LetterPreviewResponse.of(letter, schedule, picture, member)
        }
    }

    fun get(
        customUserDetails: CustomUserDetails,
        scheduleId: Long,
        letterId: Long,
    ): LetterDetailResponse {
        val memberId = customUserDetails.getId()
        val schedule = scheduleService.getById(scheduleId)
        val letter = letterService.getByCoupleAndScheduleAndId(
            customUserDetails.getCouple(),
            schedule,
            letterId,
        )

        val letterResponse = LetterResponse.of(letter, memberId)

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

    fun getLetterListToCouple(
        customUserDetails: CustomUserDetails,
        page: Int,
        size: Int,
    ): PageResponse<LetterPreviewResponse> {
        val pageRequest = PageRequest.of(page, size)
        val member = memberService.getById(customUserDetails.getId())
        val letters = letterService.findAllToCouple(customUserDetails.getCouple(), pageRequest)

        val letterPreviewResponses = letters.map { letter ->
            val picture = letter.let { pictureService.findFirstByLetterId(it.id) }
            val schedule = letter.let { scheduleService.getById(it.scheduleId) }
            LetterPreviewResponse.of(letter, schedule, picture, member)
        }

        return PageResponse.from(letterPreviewResponses)
    }

    fun getLetterListMain(
        customUserDetails: CustomUserDetails,
    ): List<MainLetterPreviewResponse> {
        val letters = letterService.findTop5ByCreatedDateDesc(customUserDetails.getCouple())
        val member = memberService.getById(customUserDetails.getId())
        return letters.map { letter ->
            val picture = pictureService.findFirstByLetterId(letter.id)
            val schedule = scheduleService.findById(letter.scheduleId)
            MainLetterPreviewResponse.of(letter, picture, schedule, member)
        }
    }
}
