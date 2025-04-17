package gomushin.backend.member.presentation

import gomushin.backend.core.CustomUserDetails
import gomushin.backend.core.common.web.response.ApiResponse
import gomushin.backend.member.facade.OnboardingFacade
import gomushin.backend.member.dto.request.OnboardingRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "온보딩", description = "OnboardingController")
class OnboardingController(
    private val onboardingFacade: OnboardingFacade,
) {

    @PostMapping(ApiPath.ONBOARDING)
    @Operation(summary = "온보딩", description = "onboarding")
    fun onboarding(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @RequestBody onboardingRequest: OnboardingRequest
    ): ApiResponse<Boolean> {
        onboardingFacade.onboarding(customUserDetails.getId(), onboardingRequest)
        return ApiResponse.success(true)
    }
}
