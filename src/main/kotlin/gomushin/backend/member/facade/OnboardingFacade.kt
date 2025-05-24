package gomushin.backend.member.facade

import gomushin.backend.member.domain.service.NotificationService
import gomushin.backend.member.domain.service.OnboardingService
import gomushin.backend.member.dto.request.OnboardingRequest
import org.springframework.stereotype.Component

@Component
class OnboardingFacade(
    private val onboardingService: OnboardingService,
    private val notificationService: NotificationService,
) {

    fun onboarding(id: Long, onboardingRequest: OnboardingRequest) {
        onboardingService.onboarding(id, onboardingRequest)
        notificationService.initNotification(id, onboardingRequest.isNotification)
    }
}
