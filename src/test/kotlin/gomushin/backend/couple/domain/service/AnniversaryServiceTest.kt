package gomushin.backend.couple.domain.service

import gomushin.backend.couple.domain.entity.Anniversary
import gomushin.backend.couple.domain.entity.Couple
import gomushin.backend.couple.domain.repository.AnniversaryRepository
import gomushin.backend.couple.dto.response.MonthlyAnniversariesResponse
import gomushin.backend.schedule.dto.response.DailyAnniversaryResponse
import gomushin.backend.schedule.dto.response.MainAnniversariesResponse
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExtendWith(MockKExtension::class)
class AnniversaryServiceTest {
    @MockK
    lateinit var anniversaryRepository: AnniversaryRepository

    @InjectMockKs
    lateinit var anniversaryService: AnniversaryService

    @DisplayName("findAnniversaries 는 Anniversary 엔티티를 Page 형태로 반환한다.")
    @Test
    fun findAnniversaries_success() {
        // given
        val couple = Couple(1L, 1L, 2L)
        val pageRequest = PageRequest.of(0, 10)
        val expectedPage = mockk<Page<Anniversary>>()

        every {
            anniversaryRepository.findAnniversaries(couple.id, pageRequest)
        } returns expectedPage

        // when
        anniversaryService.findAnniversaries(couple, pageRequest)

        // then
        verify(exactly = 1) {
            anniversaryRepository.findAnniversaries(couple.id, pageRequest)
        }
    }

    @DisplayName("findByCoupleAndDateBetween 는 Anniversary 엔티티를 List 형태로 반환한다.")
    @Test
    fun findByCoupleAndDateBetween_success() {
        // given
        val couple = Couple(1L, 1L, 2L)
        val startDate = LocalDate.of(2025, 1, 1)
        val endDate = LocalDate.of(2025, 12, 31)
        val expectedList = listOf<MainAnniversariesResponse>()

        every {
            anniversaryRepository.findByCoupleIdAndDateBetween(couple.id, startDate, endDate)
        } returns expectedList

        // when
        anniversaryService.findByCoupleAndDateBetween(couple, startDate, endDate)

        // then
        verify(exactly = 1) {
            anniversaryRepository.findByCoupleIdAndDateBetween(couple.id, startDate, endDate)
        }
    }

    @DisplayName("findByCoupleAndYearAndMonth 는 Anniversary 엔티티를 List 형태로 반환한다.")
    @Test
    fun findByCoupleAndYearAndMonth_success() {
        // given
        val couple = Couple(1L, 1L, 2L)
        val year = 2025
        val month = 1
        val expectedList = listOf<MonthlyAnniversariesResponse>()

        every {
            anniversaryRepository.findByCoupleIdAndYearAndMonth(couple.id, year, month)
        } returns expectedList

        // when
        anniversaryService.findByCoupleAndYearAndMonth(couple, year, month)

        // then
        verify(exactly = 1) {
            anniversaryRepository.findByCoupleIdAndYearAndMonth(couple.id, year, month)
        }
    }

    @DisplayName("findByCoupleAndDate 는 Anniversary 엔티티를 List 형태로 반환한다.")
    @Test
    fun findByCoupleAndDate_success() {
        // given
        val couple = Couple(1L, 1L, 2L)
        val date = LocalDate.of(2025, 1, 1)
        val expectedList = listOf<DailyAnniversaryResponse>()

        every {
            anniversaryRepository.findByCoupleIdAndDate(couple.id, date)
        } returns expectedList

        // when
        anniversaryService.findByCoupleAndDate(couple, date)

        // then
        verify(exactly = 1) {
            anniversaryRepository.findByCoupleIdAndDate(couple.id, date)
        }
    }

    @DisplayName("saveAll 은 Anniversary 엔티티를 List 형태로 저장한다.")
    @Test
    fun saveAll_success() {
        // given
        val anniversaries = listOf<Anniversary>()
        val expectedList = listOf<Anniversary>()
        every {
            anniversaryRepository.saveAll(anniversaries)
        } returns expectedList

        // when
        anniversaryService.saveAll(anniversaries)

        // then
        verify(exactly = 1) {
            anniversaryRepository.saveAll(anniversaries)
        }
    }

    @DisplayName("getUpcomingTop3Anniversaries 는 Anniversary 엔티티를 List 형태로 반환하고, 최대 3개를 반환한다.")
    @Test
    fun getUpcomingTop3Anniversaries_success() {
        // given
        val couple = Couple(1L, 1L, 2L)
        val expectedList = listOf<Anniversary>(
            Anniversary(1L, couple.id, "Anniversary 1", LocalDate.of(2025, 1, 1), 1),
            Anniversary(2L, couple.id, "Anniversary 2", LocalDate.of(2025, 2, 1), 1),
            Anniversary(3L, couple.id, "Anniversary 3", LocalDate.of(2025, 3, 1), 1),
            Anniversary(4L, couple.id, "Anniversary 4", LocalDate.of(2025, 3, 1), 1)
        )

        every {
            anniversaryRepository.findTop3UpcomingAnniversaries(couple.id)
        } returns expectedList.take(3)

        // when
        val result = anniversaryService.getUpcomingTop3Anniversaries(couple)

        // then
        assertTrue(result.size <= 3, "반환된 기념일의 개수는 최대 3개여야 합니다.")
    }

    @DisplayName("getUpcomingTop3Anniversaries 는 Anniversary 엔티티를 List 형태로 반환하고, 시간 순으로 정렬한다.")
    @Test
    fun getUpcomingTop3Anniversaries_sortedByDate() {
        // given
        val couple = Couple(1L, 1L, 2L)
        val expectedList = listOf<Anniversary>(
            Anniversary(1L, couple.id, "Anniversary 1", LocalDate.of(2025, 1, 1), 1),
            Anniversary(2L, couple.id, "Anniversary 2", LocalDate.of(2025, 2, 1), 1),
            Anniversary(3L, couple.id, "Anniversary 3", LocalDate.of(2025, 3, 1), 1),
            Anniversary(4L, couple.id, "Anniversary 4", LocalDate.of(2025, 4, 1), 1),
        )

        every {
            anniversaryRepository.findTop3UpcomingAnniversaries(couple.id)
        } returns expectedList.take(3)

        // when
        val result = anniversaryService.getUpcomingTop3Anniversaries(couple)

        // then
        assertEquals(result, expectedList.take(3))
        assertEquals(
            result.map { it.anniversaryDate }.sorted(),
            result.map { it.anniversaryDate },
            "반환된 기념일은 시간 순으로 정렬되어야 합니다."
        )
    }

    @DisplayName("deleteAllByCoupleIdAndAutoInsert는 anniversaryRepository의 deleteAllByCoupleIdAndAutoInsertTrue메서드 호출")
    @Test
    fun deleteAllByCoupleIdAndAutoInsert() {
        //given
        val couple = Couple(1L, 1L, 2L)
        every {
            anniversaryRepository.deleteAllByCoupleIdAndAutoInsertTrue(couple.id)
        } just Runs
        //when
        anniversaryService.deleteAllByCoupleAndAutoInsert(couple)
        //then
        verify(exactly = 1) {
            anniversaryRepository.deleteAllByCoupleIdAndAutoInsertTrue(couple.id)
        }
    }
}

