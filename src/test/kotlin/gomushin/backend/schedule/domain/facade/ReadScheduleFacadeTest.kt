package gomushin.backend.schedule.domain.facade

import gomushin.backend.core.CustomUserDetails
import gomushin.backend.couple.domain.entity.Couple
import gomushin.backend.couple.domain.service.AnniversaryService
import gomushin.backend.couple.dto.response.MonthlyAnniversariesResponse
import gomushin.backend.schedule.domain.entity.Letter
import gomushin.backend.schedule.domain.entity.Picture
import gomushin.backend.schedule.domain.entity.Schedule
import gomushin.backend.schedule.domain.service.LetterService
import gomushin.backend.schedule.domain.service.PictureService
import gomushin.backend.schedule.domain.service.ScheduleService
import gomushin.backend.schedule.dto.response.DailyScheduleResponse
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
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class ReadScheduleFacadeTest {

    @Mock
    private lateinit var scheduleService: ScheduleService

    @Mock
    private lateinit var anniversaryService: AnniversaryService

    @Mock
    private lateinit var letterService: LetterService

    @Mock
    private lateinit var pictureService: PictureService

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
        val mockSchedules = listOf(mock(DailyScheduleResponse::class.java))

        // when
        `when`(scheduleService.findByDate(customUserDetails.getCouple(), date)).thenReturn(mockSchedules)
        readScheduleFacade.get(customUserDetails, date)

        // then
        verify(scheduleService, times(1)).findByDate(customUserDetails.getCouple(), date)
    }

    @DisplayName("getScheduleDetail - 성공")
    @Test
    fun getScheduleDetail_success() {
        //given
        val scheduleId = 1L
        val letterId = 2L
        val mockLetter = mock(Letter::class.java)
        val mockPicture = mock(Picture::class.java)

        val schedule = Schedule(
            id = scheduleId,
            title = "일정 제목",
            fatigue = "VERT_TIRED",
            startDate = LocalDateTime.of(2025, 5, 1, 7, 0,0),
            endDate = LocalDateTime.of(2025, 5, 2,20,0,0)
        )

        //when
        `when`(mockLetter.id).thenReturn(letterId)
        `when`(mockPicture.letterId).thenReturn(letterId)
        `when`(scheduleService.getById(scheduleId)).thenReturn(schedule)
        `when`(letterService.findByCoupleAndSchedule(customUserDetails.getCouple(), schedule)).thenReturn(listOf(mockLetter))
        `when`(pictureService.findAllByLetterIds(listOf(letterId))).thenReturn(listOf(mockPicture))

        readScheduleFacade.getScheduleDetail(customUserDetails, scheduleId)

        //then
        verify(scheduleService).getById(scheduleId)
        verify(letterService).findByCoupleAndSchedule(customUserDetails.getCouple(), schedule)
        verify(pictureService).findAllByLetterIds(listOf(letterId))
    }

}
