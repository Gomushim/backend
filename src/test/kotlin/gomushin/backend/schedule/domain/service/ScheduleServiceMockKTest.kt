package gomushin.backend.schedule.domain.service

import gomushin.backend.core.common.support.SpringContextHolder
import gomushin.backend.core.configuration.env.AppEnv
import gomushin.backend.core.infrastructure.exception.BadRequestException
import gomushin.backend.schedule.domain.entity.Schedule
import gomushin.backend.schedule.domain.repository.ScheduleRepository
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.repository.findByIdOrNull
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
class ScheduleServiceMockKTest {

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

    @DisplayName("커플 멤버가 아닌 멤버가 일정을 삭제하려 하면, 에러 발생")
    @Test
    fun delete_unauthorized_throwBadRequestException() {
        // given
        val coupleId = 1L
        val userId = 2L
        val scheduleId = 3L
        val schedule = mockk<Schedule>()
        every { scheduleRepository.findByIdOrNull(scheduleId) } returns schedule
        every { schedule.coupleId } returns 4L
        every { schedule.userId } returns 5L

        // when & then
        val exception = assertThrows<BadRequestException> {
            scheduleService.delete(coupleId, userId, scheduleId)
        }
        val errorMessage = exception.error.element.message.resolved

        assertEquals("해당 일정에 대한 권한이 없어요.", errorMessage)
    }

}
