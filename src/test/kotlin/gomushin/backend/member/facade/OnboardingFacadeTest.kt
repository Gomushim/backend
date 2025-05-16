package gomushin.backend.member.facade

import gomushin.backend.member.domain.service.NotificationService
import gomushin.backend.member.domain.service.OnboardingService
import gomushin.backend.member.dto.request.OnboardingRequest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class OnboardingFacadeTest {

    @Mock
    private lateinit var onboardingService: OnboardingService

    @Mock
    private lateinit var notificationService: NotificationService

    @InjectMocks
    private lateinit var onboardingFacade: OnboardingFacade

    @DisplayName("온보딩 테스트")
    @Test
    fun onboarding_success() {
        // given
        val id = 1L
        val onboardingRequest = mock(OnboardingRequest::class.java)
        // when
        onboardingFacade.onboarding(id, onboardingRequest)
        // then
        verify(onboardingService).onboarding(id, onboardingRequest)
    }

}
