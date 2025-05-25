package gomushin.backend.schedule.facade

import gomushin.backend.core.CustomUserDetails
import gomushin.backend.couple.domain.service.AnniversaryService
import gomushin.backend.schedule.domain.service.LetterService
import gomushin.backend.schedule.domain.service.PictureService
import gomushin.backend.schedule.domain.service.ScheduleService
import gomushin.backend.schedule.dto.response.*
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class ReadScheduleFacade(
    private val scheduleService: ScheduleService,
    private val anniversaryService: AnniversaryService,
    private val letterService: LetterService,
    private val pictureService: PictureService
) {

    companion object {
        const val WEEK_DAYS = 6L
    }

    fun getList(
        customUserDetails: CustomUserDetails,
        year: Int,
        month: Int
    ): MonthlySchedulesAndAnniversariesResponse {
        val monthlySchedules = scheduleService.findByCoupleAndYearAndMonth(customUserDetails.getCouple(), year, month)
        val monthlyAnniversaries =
            anniversaryService.findByCoupleAndYearAndMonth(customUserDetails.getCouple(), year, month)
        return MonthlySchedulesAndAnniversariesResponse.of(monthlySchedules, monthlyAnniversaries)
    }

    fun get(customUserDetails: CustomUserDetails, date: LocalDate): DailySchedulesAndAnniversariesResponse {
        val dailySchedules = scheduleService.findByDate(customUserDetails.getCouple(), date)
        val dailyAnniversaries = anniversaryService.findByCoupleAndDate(customUserDetails.getCouple(), date)
        return DailySchedulesAndAnniversariesResponse.of(dailySchedules, dailyAnniversaries)
    }

    fun getDetail(customUserDetails: CustomUserDetails, scheduleId: Long): ScheduleDetailResponse {
        val schedule = scheduleService.getById(scheduleId)
        val letters = letterService.findByCoupleAndSchedule(customUserDetails.getCouple(), schedule)
        val letterIds = letters.map { it.id }
        val picturesByLetterId = pictureService.findAllByLetterIds(letterIds)
            .associateBy { it.letterId }
        val memberId = customUserDetails.getId()
        val letterPreviews = letters.map { letter ->
            LetterPreviewResponse.of(letter, schedule, picturesByLetterId[letter.id], memberId)
        }
        return ScheduleDetailResponse.of(schedule, letterPreviews)
    }

    fun getListByWeek(customUserDetails: CustomUserDetails, baseDate : LocalDate = LocalDate.now()): MainSchedulesAndAnniversariesResponse {
        val endDate = baseDate.plusDays(WEEK_DAYS)
        val schedules = scheduleService.findByCoupleAndDateBetween(
            customUserDetails.getCouple(),
            baseDate,
            endDate
        )
        val anniversaries = anniversaryService.findByCoupleAndDateBetween(
            customUserDetails.getCouple(),
            baseDate,
            endDate
        )

        return MainSchedulesAndAnniversariesResponse.of(
            schedules,
            anniversaries
        )
    }
}
