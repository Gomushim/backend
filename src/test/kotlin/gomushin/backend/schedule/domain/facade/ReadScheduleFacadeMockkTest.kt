package gomushin.backend.schedule.domain.facade

import gomushin.backend.core.CustomUserDetails
import gomushin.backend.couple.domain.entity.Couple
import gomushin.backend.couple.domain.service.AnniversaryService
import gomushin.backend.couple.dto.response.MonthlyAnniversariesResponse
import gomushin.backend.schedule.domain.service.LetterService
import gomushin.backend.schedule.domain.service.PictureService
import gomushin.backend.schedule.domain.service.ScheduleService
import gomushin.backend.schedule.dto.response.*
import gomushin.backend.schedule.facade.ReadScheduleFacade
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDate

@ExtendWith(MockKExtension::class)
class ReadScheduleFacadeMockkTest {

    @MockK
    lateinit var scheduleService: ScheduleService

    @MockK
    lateinit var anniversaryService: AnniversaryService

    @MockK
    lateinit var letterService: LetterService

    @MockK
    lateinit var pictureService: PictureService

    @InjectMockKs
    lateinit var readScheduleFacade: ReadScheduleFacade

    private val customUserDetails = mockk<CustomUserDetails>()

    private val couple = mockk<Couple>()

    init {
        every { customUserDetails.getCouple() } returns couple
        every { customUserDetails.getId() } returns 1L
    }

    @DisplayName("getList - 성공")
    @Test
    fun getList_success() {
        // given
        val year = 2025
        val month = 4
        val monthlySchedulesResponse = mockk<MonthlySchedulesResponse>()
        val monthlyAnniversariesResponse = mockk<MonthlyAnniversariesResponse>()

        // when
        every {
            scheduleService.findByCoupleAndYearAndMonth(
                couple,
                year,
                month
            )
        } returns listOf(monthlySchedulesResponse)

        every {
            anniversaryService.findByCoupleAndYearAndMonth(
                couple,
                year,
                month
            )
        } returns listOf(monthlyAnniversariesResponse)
        readScheduleFacade.getList(customUserDetails, year, month)

        // then
        verify(exactly = 1) {
            scheduleService.findByCoupleAndYearAndMonth(couple, year, month)
            anniversaryService.findByCoupleAndYearAndMonth(couple, year, month)
        }
    }

    @DisplayName("getListByWeek - 성공")
    @Test
    fun getListByWeek_success() {
        // given
        val date = LocalDate.of(2025, 5, 22)
        val mainSchedulesResponse = mockk<MainSchedulesResponse>()
        val mainAnniversariesResponse = mockk<MainAnniversariesResponse>()

        // when
        every {
            scheduleService.findByCoupleAndDateBetween(
                couple,
                date,
                date.plusDays(6)
            )
        } returns listOf(mainSchedulesResponse)

        every {
            anniversaryService.findByCoupleAndDateBetween(
                couple,
                date,
                date.plusDays(6)
            )
        } returns listOf(mainAnniversariesResponse)


        readScheduleFacade.getListByWeek(customUserDetails, date)

        // then
        verify(exactly = 1) {
            scheduleService.findByCoupleAndDateBetween(
                couple,
                date,
                date.plusDays(6)
            )
            anniversaryService.findByCoupleAndDateBetween(
                couple,
                date,
                date.plusDays(6)
            )
        }
    }

    @DisplayName("get - 성공")
    @Test
    fun get_success() {
        // given
        val date = LocalDate.of(2025, 4, 1)
        val mockSchedules = listOf(mockk<DailyScheduleResponse>())
        val mockAnniversaries = listOf(mockk<DailyAnniversaryResponse>())

        // when
        every { scheduleService.findByDate(couple, date) } returns mockSchedules
        every { anniversaryService.findByCoupleAndDate(couple, date) } returns mockAnniversaries
        readScheduleFacade.get(customUserDetails, date)

        // then
        verify(exactly = 1) {
            scheduleService.findByDate(couple, date)
            anniversaryService.findByCoupleAndDate(couple, date)
        }
    }
}
