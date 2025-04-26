package gomushin.backend.member.facade

import gomushin.backend.core.CustomUserDetails
import gomushin.backend.member.domain.service.LeaveService
import org.springframework.stereotype.Component

@Component
class LeaveFacade(
    private val leaveService: LeaveService
) {
    fun leave(customUserDetails: CustomUserDetails) {
        leaveService.leave(customUserDetails.getId(), customUserDetails.getCouple().id)
    }
}