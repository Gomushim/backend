package gomushin.backend.schedule.domain.facade

import gomushin.backend.core.CustomUserDetails
import gomushin.backend.couple.domain.entity.Couple
import gomushin.backend.schedule.domain.entity.Letter
import gomushin.backend.schedule.domain.entity.Picture
import gomushin.backend.schedule.domain.entity.Schedule
import gomushin.backend.schedule.domain.service.CommentService
import gomushin.backend.schedule.domain.service.LetterService
import gomushin.backend.schedule.domain.service.PictureService
import gomushin.backend.schedule.domain.service.ScheduleService
import gomushin.backend.schedule.facade.ReadLetterFacade
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import kotlin.test.Test

@ExtendWith(MockitoExtension::class)
class ReadLetterFacadeTest {

    @Mock
    lateinit var letterService: LetterService

    @Mock
    lateinit var scheduleService: ScheduleService

    @Mock
    lateinit var commentService: CommentService

    @Mock
    lateinit var pictureService: PictureService

    @InjectMocks
    lateinit var readLetterFacade: ReadLetterFacade


    @DisplayName("getList - 성공")
    @Test
    fun getList_success() {
        // given
        val scheduleId = 1L
        val customUserDetails = mock(CustomUserDetails::class.java)
        val schedule = mock(Schedule::class.java)
        val letter = mock(Letter::class.java)

        // when
        `when`(customUserDetails.getCouple()).thenReturn(mock(Couple::class.java))
        `when`(scheduleService.getById(scheduleId)).thenReturn(schedule)
        `when`(
            letterService.findByCoupleAndSchedule(
                customUserDetails.getCouple(),
                schedule
            )
        ).thenReturn(
            listOf(
                letter
            )
        )
        `when`(pictureService.findFirstByLetterId(letter.id)).thenReturn(mock(Picture::class.java))

        readLetterFacade.getList(
            customUserDetails,
            scheduleId
        )

        // then
        verify(scheduleService, times(1)).getById(scheduleId)
        verify(letterService, times(1)).findByCoupleAndSchedule(
            customUserDetails.getCouple(),
            schedule
        )
        verify(pictureService, times(1)).findFirstByLetterId(letter.id)
    }

    // TODO: get 테스트 작성(성공)
}
