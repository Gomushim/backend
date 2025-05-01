package gomushin.backend.couple.facade

import gomushin.backend.core.CustomUserDetails
import gomushin.backend.couple.domain.service.AnniversaryService
import gomushin.backend.couple.dto.response.MainAnniversaryResponse
import org.springframework.stereotype.Component

@Component
class AnniversaryFacade(
    private val anniversaryService: AnniversaryService,
) {
    fun getAnniversaryListMain(customUserDetails: CustomUserDetails): List<MainAnniversaryResponse> {
        val anniversaries = anniversaryService.getUpcomingTop3Anniversaries(customUserDetails.getCouple())
        return anniversaries.map { MainAnniversaryResponse.of(it) }
    }
}
