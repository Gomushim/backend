package gomushin.backend.schedule.presentation

import gomushin.backend.core.CustomUserDetails
import gomushin.backend.core.common.web.response.ApiResponse
import gomushin.backend.schedule.dto.UpsertScheduleRequest
import gomushin.backend.schedule.facade.UpsertAndDeleteScheduleFacade
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@Tag(name = "일정", description = "UpsertAndDeleteScheduleController")
class UpsertAndDeleteScheduleController(
    private val upsertAndDeleteScheduleFacade: UpsertAndDeleteScheduleFacade,
) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(ApiPath.SCHEDULES)
    @Operation(summary = "일정 수정하거나 추가하기", description = "upsertSchedule")
    fun upsertSchedule(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @RequestBody upsertScheduleRequest: UpsertScheduleRequest
    ): ApiResponse<Boolean> {
        upsertAndDeleteScheduleFacade.upsert(customUserDetails, upsertScheduleRequest)
        return ApiResponse.success(true)
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(ApiPath.SCHEDULE)
    @Operation(summary = "일정 삭제하기", description = "deleteSchedule")
    fun deleteSchedule(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @PathVariable scheduleId: Long
    ): ApiResponse<Boolean> {
        upsertAndDeleteScheduleFacade.delete(customUserDetails, scheduleId)
        return ApiResponse.success(true)
    }
}
