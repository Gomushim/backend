package gomushin.backend.couple.facade

import gomushin.backend.core.CustomUserDetails
import gomushin.backend.core.common.web.PageResponse
import gomushin.backend.couple.domain.service.AnniversaryService
import gomushin.backend.couple.dto.request.ReadAnniversariesRequest
import gomushin.backend.couple.dto.response.MainAnniversaryResponse
import gomushin.backend.couple.dto.response.TotalAnniversaryResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class AnniversaryFacade(
    private val anniversaryService: AnniversaryService,
    @Value("\${server.url}")
    private val baseUrl: String,
) {
    fun getAnniversaryListMain(customUserDetails: CustomUserDetails): List<MainAnniversaryResponse> {
        val anniversaries = anniversaryService.getUpcomingTop3Anniversaries(customUserDetails.getCouple())
        return anniversaries.map { MainAnniversaryResponse.of(it) }
    }

    fun getAnniversaryList(
        customUserDetails: CustomUserDetails,
        readAnniversariesRequest: ReadAnniversariesRequest,
    ): PageResponse<TotalAnniversaryResponse> {
        val anniversaries =
            anniversaryService.findAnniversaries(customUserDetails.getCouple(), readAnniversariesRequest)
        val anniversaryResponses = anniversaries.map { anniversary ->
            TotalAnniversaryResponse.of(anniversary)
        }
        val isLastPage = anniversaryResponses.size < readAnniversariesRequest.take

        val hasData = anniversaryResponses.isNotEmpty()

        val nextUrl = if (!isLastPage && hasData) {
            "${baseUrl}/v1/anniversaries?key=${anniversaryResponses.last().id}&take=${readAnniversariesRequest.take}&page=${readAnniversariesRequest.take}"
        } else {
            null
        }

        return PageResponse.of(
            data = anniversaryResponses,
            after = if (hasData) anniversaryResponses.last().id else null,
            count = anniversaryResponses.size,
            next = nextUrl,
            isLastPage = isLastPage
        )
    }
}
