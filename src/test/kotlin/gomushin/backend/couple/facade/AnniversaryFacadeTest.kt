package gomushin.backend.couple.facade

import gomushin.backend.core.CustomUserDetails
import gomushin.backend.couple.domain.entity.Anniversary
import gomushin.backend.couple.domain.entity.Couple
import gomushin.backend.couple.domain.service.AnniversaryService
import gomushin.backend.schedule.dto.response.MainAnniversariesResponse
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.time.LocalDate

@ExtendWith(MockKExtension::class)
class AnniversaryFacadeTest {
    @MockK
    lateinit var anniversaryService: AnniversaryService

    @InjectMockKs
    lateinit var anniversaryFacade: AnniversaryFacade

    @DisplayName("getAnniversaryListMain 은 AnniversaryService 의 getUpcomingTop3Anniversaries 를 호출한다.")
    @Test
    fun getAnniversaryListMain_success() {
        // given
        val customUserDetails = mockk<CustomUserDetails>()
        val couple = mockk<Couple>()
        val anniversaryList = listOf<Anniversary>()
        val anniversaryResponseList = listOf<MainAnniversariesResponse>()

        every {
            customUserDetails.getCouple()
        } returns couple

        every {
            anniversaryService.getUpcomingTop3Anniversaries(couple)
        } returns anniversaryList

        // when
        val result = anniversaryFacade.getAnniversaryListMain(customUserDetails)

        // then
        verify(exactly = 1) {
            anniversaryService.getUpcomingTop3Anniversaries(couple)
        }
        assert(result == anniversaryResponseList)
    }

    @DisplayName("getAnniversaryList 은 AnniversaryService 의 findAnniversaries 를 호출한다.")
    @Test
    fun getAnniversaryList_success() {
        // given
        val customUserDetails = mockk<CustomUserDetails>()
        val couple = mockk<Couple>()
        val page = 0
        val size = 10
        val pageRequest = PageRequest.of(page, size)

        val content = listOf(
            Anniversary(1L, 1L, "Anniversary 1", LocalDate.of(2023, 10, 1), 1),
            Anniversary(2L, 2L, "Anniversary 2", LocalDate.of(2023, 10, 2), 1)
        )
        val anniversaries = PageImpl(content)

        every { customUserDetails.getCouple() } returns couple
        every {
            anniversaryService.findAnniversaries(couple, pageRequest)
        } returns anniversaries

        // when
        val result = anniversaryFacade.getAnniversaryList(customUserDetails, page, size)

        // then
        verify(exactly = 1) {
            anniversaryService.findAnniversaries(couple, pageRequest)
        }
        assertThat(result.data).hasSize(2)
        assertThat(result.totalPages).isEqualTo(1)
    }
}
