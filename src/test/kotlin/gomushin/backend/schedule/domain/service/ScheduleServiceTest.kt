package gomushin.backend.schedule.domain.service

import gomushin.backend.schedule.domain.entity.Schedule
import gomushin.backend.schedule.domain.repository.ScheduleRepository
import gomushin.backend.schedule.dto.UpsertScheduleRequest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockitoExtension::class)
class ScheduleServiceTest {

    @Mock
    private lateinit var scheduleRepository: ScheduleRepository

    @InjectMocks
    private lateinit var scheduleService: ScheduleService

    @DisplayName("일정 저장 테스트")
    @Test
    fun insert_success() {
        // given
        val coupleId = 1L
        val userId = 1L
        val upsertScheduleRequest = mock(UpsertScheduleRequest::class.java)
        `when`(upsertScheduleRequest.toEntity(coupleId, userId)).thenReturn(mock(Schedule::class.java))
        `when`(scheduleRepository.save(any(Schedule::class.java))).thenReturn(mock(Schedule::class.java))

        // when
        scheduleService.upsert(null, coupleId, userId, upsertScheduleRequest)

        // then
        verify(scheduleRepository).save(any(Schedule::class.java))
    }

    @DisplayName("일정 수정 테스트")
    @Test
    fun update_success() {
        // given
        val id = 1L
        val coupleId = 1L
        val userId = 1L
        val upsertScheduleRequest = mock(UpsertScheduleRequest::class.java)
        val mockSchedule = mock(Schedule::class.java)
        `when`(scheduleRepository.findById(id)).thenReturn(Optional.of(mockSchedule))

        // when
        scheduleService.upsert(id, coupleId, userId, upsertScheduleRequest)

        // then
        verify(scheduleRepository).findById(id)
        verify(scheduleRepository, never()).save(any(Schedule::class.java))
    }

    @DisplayName("일정 삭제 테스트")
    @Test
    fun delete_success() {
        // given
        val coupleId = 1L
        val userId = 1L
        val scheduleId = 1L
        val mockSchedule = Schedule(
            id = scheduleId,
            coupleId = coupleId,
            userId = userId,
            startDate = LocalDateTime.of(2023, 1, 1, 0, 0),
            endDate = LocalDateTime.of(2023, 1, 2, 0, 0),
            content = "Test Schedule",
            isAllDay = false,
            fatigue = "VERY_TIRED",
        )
        `when`(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(mockSchedule))

        // when
        scheduleService.delete(coupleId, userId, scheduleId)

        // then
        verify(scheduleRepository).deleteById(scheduleId)
    }
}
