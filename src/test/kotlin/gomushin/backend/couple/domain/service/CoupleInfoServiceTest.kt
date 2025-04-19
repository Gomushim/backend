package gomushin.backend.couple.domain.service

import gomushin.backend.core.infrastructure.exception.BadRequestException
import gomushin.backend.couple.domain.entity.Couple
import gomushin.backend.couple.domain.repository.CoupleRepository
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate


@ExtendWith(MockitoExtension::class)
class CoupleInfoServiceTest {
    @Mock
    private lateinit var coupleRepository: CoupleRepository

    @InjectMocks
    private lateinit var coupleInfoService: CoupleInfoService

    @DisplayName("getGrade - 성공(invitorId가 주어졌을 떄)")
    @Test
    fun getGrade_success_1() {
        // given
        val coupleId = 1L
        val invitorId = 1L
        val couple = Couple(
                id = coupleId,
                invitorId = 1L,
                inviteeId = 2L,
                militaryStartDate = LocalDate.of(2021,5,24)
        )

        Mockito.`when`(coupleRepository.findByInvitorId(invitorId)).thenReturn(couple)

        // when
        coupleInfoService.getGrade(invitorId)
    }

    @DisplayName("getGrade - 성공(inviteeId가 주어졌을 떄)")
    @Test
    fun getGrade_success_2() {
        // given
        val coupleId = 1L
        val inviteeId = 2L
        val couple = Couple(
                id = coupleId,
                invitorId = 1L,
                inviteeId = 2L,
                militaryStartDate = LocalDate.of(2021,5,24)
        )

        `when`(coupleRepository.findByInvitorId(anyLong())).thenReturn(null)
        `when`(coupleRepository.findByInvitorId(inviteeId)).thenReturn(couple)


        // when
        coupleInfoService.getGrade(inviteeId)
    }

//    @DisplayName("getGrade - 실패(couple이 아닐 때)")
//    @Test
//    fun getGrade_fail() {
//        // given
//        val invitorId = 1L
//
//        `when`(coupleRepository.findByInvitorId(anyLong())).thenReturn(null)
//        `when`(coupleRepository.findByInviteeId(anyLong())).thenReturn(null)
//
//        // when & then
//        val exception = assertThrows(BadRequestException::class.java) {
//            coupleInfoService.getGrade(invitorId)
//        }
//        assert(exception.message == "saranggun.couple.not-connected")
//    }

    @DisplayName("computeGrade - 성공")
    @Test
    fun computeGrade_success() {
        // given
        val gradeMilitaryStartDate = LocalDate.of(2021, 5, 24)

        val gradeOneToday = LocalDate.of(2021, 6, 1)
        val gradeTwoToday = LocalDate.of(2021, 7, 1)
        val gradeThreeToday = LocalDate.of(2022, 2, 1)
        val gradeFourToday = LocalDate.of(2022,8,1)

        // when
        val resultGradeOne = coupleInfoService.computeGrade(gradeMilitaryStartDate, gradeOneToday)
        val resultGradeTwo = coupleInfoService.computeGrade(gradeMilitaryStartDate, gradeTwoToday)
        val resultGradeThree = coupleInfoService.computeGrade(gradeMilitaryStartDate, gradeThreeToday)
        val resultGradeFour = coupleInfoService.computeGrade(gradeMilitaryStartDate, gradeFourToday)

        // then
        assert(resultGradeOne == 1)
        assert(resultGradeTwo == 2)
        assert(resultGradeThree == 3)
        assert(resultGradeFour == 4)
    }
}