package gomushin.backend.schedule.domain.facade

import gomushin.backend.core.CustomUserDetails
import gomushin.backend.couple.domain.entity.Couple
import gomushin.backend.couple.domain.service.AnniversaryService
import gomushin.backend.couple.dto.response.MonthlyAnniversariesResponse
import gomushin.backend.schedule.domain.entity.Schedule
import gomushin.backend.schedule.domain.service.ScheduleService
import gomushin.backend.schedule.dto.response.MonthlySchedulesResponse
import gomushin.backend.schedule.facade.ReadScheduleFacade
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
class ReadScheduleFacadeTest {

    @Mock
    private lateinit var scheduleService: ScheduleService

    @Mock
    private lateinit var anniversaryService: AnniversaryService

    @InjectMocks
    private lateinit var readScheduleFacade: ReadScheduleFacade

    private val customUserDetails = mock(CustomUserDetails::class.java)

    @BeforeEach
    fun setUp() {
        `when`(customUserDetails.getCouple()).thenReturn(mock(Couple::class.java))
    }

    @DisplayName("getList - 성공")
    @Test
    fun getList_success() {
        // given
        val year = 2025
        val month = 4
        val monthlySchedulesResponse = mock(MonthlySchedulesResponse::class.java)
        val monthlyAnniversariesResponse = mock(MonthlyAnniversariesResponse::class.java)

        // when
        `when`(scheduleService.findByCoupleIdAndYearAndMonth(customUserDetails.getCouple(), year, month))
            .thenReturn(listOf(monthlySchedulesResponse))
        `when`(anniversaryService.findByCoupleIdAndYearAndMonth(customUserDetails.getCouple(), year, month))
            .thenReturn(listOf(monthlyAnniversariesResponse))
        readScheduleFacade.getList(customUserDetails, year, month)

        // then
        verify(scheduleService, times(1)).findByCoupleIdAndYearAndMonth(customUserDetails.getCouple(), year, month)
        verify(anniversaryService, times(1)).findByCoupleIdAndYearAndMonth(customUserDetails.getCouple(), year, month)
    }

    @DisplayName("get - 성공")
    @Test
    fun get_success() {
        // given
        val date = LocalDate.of(2025, 4, 1)
        val mockSchedules = listOf(mock(Schedule::class.java))

        // when
        `when`(scheduleService.findByDate(customUserDetails.getCouple(), date)).thenReturn(mockSchedules)
        readScheduleFacade.get(customUserDetails, date)

        // then
        verify(scheduleService, times(1)).findByDate(customUserDetails.getCouple(), date)
    }

}
