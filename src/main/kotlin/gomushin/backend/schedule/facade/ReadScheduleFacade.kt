package gomushin.backend.schedule.facade

import gomushin.backend.core.CustomUserDetails
import gomushin.backend.couple.domain.service.AnniversaryService
import gomushin.backend.schedule.domain.entity.Schedule
import gomushin.backend.schedule.domain.service.ScheduleService
import gomushin.backend.schedule.dto.response.MonthlySchedulesAndAnniversariesResponse
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class ReadScheduleFacade(
    private val scheduleService: ScheduleService,
    private val anniversaryService: AnniversaryService,
) {

    fun getList(
        customUserDetails: CustomUserDetails,
        year: Int,
        month: Int
    ): MonthlySchedulesAndAnniversariesResponse {
        val monthlySchedules = scheduleService.findByCoupleIdAndYearAndMonth(customUserDetails.getCouple(), year, month)
        val monthlyAnniversaries =
            anniversaryService.findByCoupleIdAndYearAndMonth(customUserDetails.getCouple(), year, month)
        return MonthlySchedulesAndAnniversariesResponse.of(monthlySchedules, monthlyAnniversaries)
    }

    fun get(customUserDetails: CustomUserDetails, date: LocalDate): List<Schedule> {
        return scheduleService.findByDate(customUserDetails.getCouple(), date)
    }
}
