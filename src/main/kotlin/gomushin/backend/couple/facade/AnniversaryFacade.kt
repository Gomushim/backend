package gomushin.backend.couple.facade

import gomushin.backend.core.CustomUserDetails
import gomushin.backend.core.common.web.PageResponse
import gomushin.backend.core.infrastructure.exception.BadRequestException
import gomushin.backend.couple.domain.service.AnniversaryService
import gomushin.backend.couple.dto.request.UpdateAnniversaryRequest
import gomushin.backend.couple.dto.response.AnniversaryDetailResponse
import gomushin.backend.couple.dto.response.MainAnniversaryResponse
import gomushin.backend.couple.dto.response.TotalAnniversaryResponse
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component

@Component
class AnniversaryFacade(
    private val anniversaryService: AnniversaryService,
) {
    fun getAnniversaryListMain(customUserDetails: CustomUserDetails): List<MainAnniversaryResponse> {
        val anniversaries = anniversaryService.getUpcomingTop3Anniversaries(customUserDetails.getCouple())
        return anniversaries.map { MainAnniversaryResponse.of(it) }
    }

    fun getAnniversaryList(
        customUserDetails: CustomUserDetails,
        page: Int,
        size: Int,
    ): PageResponse<TotalAnniversaryResponse> {
        val pageRequest = PageRequest.of(page, size)
        val anniversaries =
            anniversaryService.findAnniversaries(customUserDetails.getCouple(), pageRequest)
        val anniversaryResponses = anniversaries.map { anniversary ->
            TotalAnniversaryResponse.of(anniversary)
        }

        return PageResponse.from(anniversaryResponses)
    }

    fun get(customUserDetails: CustomUserDetails, anniversaryId: Long): AnniversaryDetailResponse {
        val anniversary = anniversaryService.getById(anniversaryId)
        if (anniversary.coupleId != customUserDetails.getCouple().id) {
            throw BadRequestException("sarangggun.anniversary.unauthorized")
        }
        return AnniversaryDetailResponse.of(anniversary)
    }

    fun delete(customUserDetails: CustomUserDetails, anniversaryId: Long) {
        anniversaryService.delete(customUserDetails.getCouple(), anniversaryId)
    }

    fun updateAnniversary(
        customUserDetails: CustomUserDetails,
        anniversaryId: Long,
        updateAnniversaryRequest: UpdateAnniversaryRequest,
    ) {
        anniversaryService.update(customUserDetails.getCouple(), anniversaryId, updateAnniversaryRequest)
    }
}
