package gomushin.backend.schedule.presentation

import gomushin.backend.core.CustomUserDetails
import gomushin.backend.core.common.web.response.ApiResponse
import gomushin.backend.schedule.dto.response.DailySchedulesAndAnniversariesResponse
import gomushin.backend.schedule.dto.response.MonthlySchedulesAndAnniversariesResponse
import gomushin.backend.schedule.dto.response.ScheduleDetailResponse
import gomushin.backend.schedule.facade.ReadScheduleFacade
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@Tag(name = "일정 조회", description = "ReadScheduleController")
class ReadScheduleController(
    private val readScheduleFacade: ReadScheduleFacade
) {

    @GetMapping(ApiPath.SCHEDULES)
    @Operation(summary = "특정 달의 일정 리스트 가져오기", description = "getScheduleList")
    fun getScheduleList(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @RequestParam year: Int,
        @RequestParam month: Int,
    ): ApiResponse<MonthlySchedulesAndAnniversariesResponse> {
        val schedules = readScheduleFacade.getList(customUserDetails, year, month)
        return ApiResponse.success(schedules)
    }

    @GetMapping(ApiPath.SCHEDULES_BY_DATE)
    @Operation(summary = "특정 날짜의 일정 가져오기", description = "getSchedule")
    fun getSchedule(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @RequestParam date: LocalDate,
    ): ApiResponse<DailySchedulesAndAnniversariesResponse> {
        val schedules = readScheduleFacade.get(customUserDetails, date)
        return ApiResponse.success(schedules)
    }

    @GetMapping(ApiPath.SCHEDULE_DETAIL)
    @Operation(summary = "특정 스케쥴 상세조회", description = "getScheduleDetail")
    fun getScheduleDetail(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @PathVariable scheduleId: Long
    ): ApiResponse<ScheduleDetailResponse> {
        val scheduleDetails = readScheduleFacade.getScheduleDetail(customUserDetails, scheduleId)
        return ApiResponse.success(scheduleDetails)
    }
}
