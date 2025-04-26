package gomushin.backend.schedule.facade

import gomushin.backend.core.CustomUserDetails
import gomushin.backend.schedule.domain.service.ScheduleService
import gomushin.backend.schedule.dto.request.UpsertScheduleRequest
import org.springframework.stereotype.Component

@Component
class UpsertAndDeleteScheduleFacade(
    private val scheduleService: ScheduleService,
) {

    fun upsert(customUserDetails: CustomUserDetails, upsertScheduleRequest: UpsertScheduleRequest) {
        scheduleService.upsert(
            upsertScheduleRequest.id,
            customUserDetails.getCouple().id,
            customUserDetails.getId(),
            upsertScheduleRequest
        )
    }

    fun delete(customUserDetails: CustomUserDetails, scheduleId: Long) {
        scheduleService.delete(customUserDetails.getCouple().id, customUserDetails.getId(), scheduleId)
    }
}
