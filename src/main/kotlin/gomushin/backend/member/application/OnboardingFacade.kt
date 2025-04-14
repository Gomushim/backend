package gomushin.backend.member.application

import gomushin.backend.member.domain.service.OnboardingService
import gomushin.backend.member.presentation.dto.request.OnboardingRequest
import org.springframework.stereotype.Component

@Component
class OnboardingFacade(
    private val onboardingService: OnboardingService,
) {

    fun onboarding(id: Long, onboardingRequest: OnboardingRequest) = onboardingService.onboarding(id, onboardingRequest)
}
