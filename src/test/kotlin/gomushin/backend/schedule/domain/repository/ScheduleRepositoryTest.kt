package gomushin.backend.schedule.domain.repository

import gomushin.backend.couple.domain.entity.Couple
import gomushin.backend.schedule.domain.entity.Schedule
import gomushin.backend.schedule.dto.response.MonthlySchedulesResponse
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.hamcrest.Matchers.containsInAnyOrder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.Month

@DataJpaTest
@Transactional
@ExtendWith(MockitoExtension::class)
class ScheduleRepositoryTest @Autowired constructor(
    val scheduleRepository : ScheduleRepository
){
    private lateinit var couple : Couple
    @BeforeEach
    fun setup() {
        couple = Couple.of(1L, 2L);
        val scheduleList = listOf(
            Schedule.of(
                couple.id,
                1L,
                "시작월과 끝월이 같음",
                LocalDateTime.of(2025, 7, 21, 0, 0, 0, 0),
                LocalDateTime.of(2025, 7,22,0,0,0,0),
                "VERY_TIRED",
                true),
            Schedule.of(
                couple.id,
                1L,
                "시작월과 끝월이 다름",
                LocalDateTime.of(2025, 7, 21, 0, 0, 0, 0),
                LocalDateTime.of(2025, 8,1,0,0,0,0),
                "VERY_TIRED",
                true)
        )
        scheduleRepository.saveAll(scheduleList);
    }
    @DisplayName("startDate 기준으로 검색이 되는지 테스트")
    @Test
    fun filters_schedules_by_startDate_within_given_year_and_month() {
        //when
        val responseList = scheduleRepository.findByCoupleIdAndYearAndMonth(couple.id, 2025, 7)
        //then
        assertEquals(2, responseList.size)
        assertTrue(
            responseList.stream().allMatch { i: MonthlySchedulesResponse ->
                (i.startDate.year == 2025 && i.endDate.year == 2025)
                        &&
                        (i.startDate.month == Month.JULY || i.endDate.month == Month.JULY)
            }
        )
        val actualContents: List<String> = responseList.map { it.title }
        MatcherAssert.assertThat(actualContents, containsInAnyOrder("시작월과 끝월이 같음", "시작월과 끝월이 다름"))
    }

    @DisplayName("endDate 기준으로 검색이 되는지 테스트")
    @Test
    fun filters_schedules_by_endDate_within_given_year_and_month() {
        //when
        val responseList = scheduleRepository.findByCoupleIdAndYearAndMonth(couple.id, 2025, 8)
        //then
        assertEquals(1, responseList.size)
        assertTrue(
            responseList.stream().allMatch { i: MonthlySchedulesResponse ->
                (i.startDate.year == 2025 && i.endDate.year == 2025)
                        &&
                        (i.startDate.month == Month.JULY || i.endDate.month == Month.JULY)
            }
        )
        val actualContents: List<String> = responseList.map { it.title }
        MatcherAssert.assertThat(actualContents, containsInAnyOrder("시작월과 끝월이 다름"))
    }
}