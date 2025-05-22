package gomushin.backend.schedule.domain.service

import gomushin.backend.core.common.support.SpringContextHolder
import gomushin.backend.core.configuration.env.AppEnv
import gomushin.backend.core.infrastructure.exception.BadRequestException
import gomushin.backend.schedule.domain.entity.Schedule
import gomushin.backend.schedule.domain.repository.ScheduleRepository
import gomushin.backend.schedule.dto.request.UpsertScheduleRequest
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDateTime
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
class ScheduleServiceTest {

    @MockK
    lateinit var scheduleRepository: ScheduleRepository

    private lateinit var scheduleService: ScheduleService

    @BeforeEach
    fun setUp() {
        // 확장함수 모킹
        mockkStatic("org.springframework.data.repository.CrudRepositoryExtensionsKt")

        // 서비스 인스턴스 직접 생성
        // 확장함수를 사용하는 경우, @InjectMockks가 아닌 직접 생성해야 함
        scheduleService = ScheduleService(scheduleRepository)

        // SpringContextHolder 모킹
        mockkObject(SpringContextHolder)
        val mockAppEnv = mockk<AppEnv>()
        every { SpringContextHolder.getBean(AppEnv::class.java) } returns mockAppEnv
        every { mockAppEnv.getId() } returns "test"
    }

    @Nested
    inner class UpsertTest {
        @DisplayName("일정 저장 테스트 - 성공")
        @Test
        fun insert_success() {
            // given
            val coupleId = 1L
            val userId = 1L
            val upsertScheduleRequest = mockk<UpsertScheduleRequest>()
            every { upsertScheduleRequest.toEntity(coupleId, userId) } returns mockk<Schedule>()
            every { scheduleRepository.save(any()) } returns mockk<Schedule>()

            // when
            scheduleService.upsert(null, coupleId, userId, upsertScheduleRequest)

            // then
            verify { scheduleRepository.save(any()) }
        }

        @DisplayName("일정 수정 테스트 - 성공")
        @Test
        fun update_success() {
            // given
            val id = 1L
            val scheduleId = 1L
            val coupleId = 1L
            val userId = 1L
            val upsertScheduleRequest = mockk<UpsertScheduleRequest>()
            val schedule = Schedule(
                id = scheduleId,
                coupleId = coupleId,
                userId = userId,
                startDate = LocalDateTime.of(2023, 1, 1, 0, 0),
                endDate = LocalDateTime.of(2023, 1, 2, 0, 0),
                title = "Test Schedule",
                isAllDay = false,
                fatigue = "VERY_TIRED",
            )


            every { upsertScheduleRequest.title } returns "Updated Schedule"
            every { upsertScheduleRequest.fatigue } returns "TIRED"
            every { upsertScheduleRequest.isAllDay } returns true
            every { upsertScheduleRequest.id } returns id
            every { upsertScheduleRequest.startDate } returns LocalDateTime.of(2023, 1, 1, 0, 0)
            every { upsertScheduleRequest.endDate } returns LocalDateTime.of(2023, 1, 2, 0, 0)

            every { scheduleRepository.findByIdOrNull(id) } returns schedule
            every { scheduleRepository.save(schedule) } returns schedule

            // when
            scheduleService.upsert(id, coupleId, userId, upsertScheduleRequest)

            // then
            verify { scheduleRepository.findByIdOrNull(id) }
            verify(exactly = 0) {
                scheduleRepository.save(any())
            }
        }
    }

    @Nested
    inner class ReadTest {
        @DisplayName("getById() 경우, 존재하지 않는 id로 조회 시 BadRequestException 발생")
        @Test
        fun getById_notExistId_throwBadRequestException() {
            // given
            val id = 1L
            every {
                scheduleRepository.findByIdOrNull(id)
            } returns null

            // when & then
            val exception = assertThrows<BadRequestException> {
                scheduleService.getById(id)
            }
            val errorMessage = exception.error.element.message.resolved

            assertEquals("해당 일정이 존재하지 않아요.", errorMessage)
        }
    }

    @Nested
    inner class DeleteTest {
        @DisplayName("일정 삭제 테스트 - 성공")
        @Test
        fun delete_success() {
            // given
            val coupleId = 1L
            val userId = 1L
            val scheduleId = 1L
            val schedule = Schedule(
                id = scheduleId,
                coupleId = coupleId,
                userId = userId,
                startDate = LocalDateTime.of(2023, 1, 1, 0, 0),
                endDate = LocalDateTime.of(2023, 1, 2, 0, 0),
                title = "Test Schedule",
                isAllDay = false,
                fatigue = "VERY_TIRED",
            )

            every { scheduleRepository.findByIdOrNull(scheduleId) } returns schedule
            justRun { scheduleRepository.deleteById(scheduleId) }

            // when
            scheduleService.delete(coupleId, userId, scheduleId)

            // then
            verify { scheduleRepository.deleteById(scheduleId) }
        }

    }
}
