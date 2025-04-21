package gomushin.backend.couple.domain.service

import gomushin.backend.couple.domain.entity.Couple
import gomushin.backend.couple.domain.repository.CoupleRepository
import gomushin.backend.member.domain.entity.Member
import gomushin.backend.member.domain.repository.MemberRepository
import gomushin.backend.member.domain.value.Provider
import gomushin.backend.member.domain.value.Role
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDate
import java.util.*
import kotlin.test.assertEquals


@ExtendWith(MockitoExtension::class)
class CoupleInfoServiceTest {
    @Mock
    private lateinit var coupleRepository: CoupleRepository
    @Mock
    private lateinit var memberRepository: MemberRepository

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

        `when`(coupleRepository.findByMemberId(invitorId)).thenReturn(couple)

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

        `when`(coupleRepository.findByMemberId(inviteeId)).thenReturn(couple)


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

    @DisplayName("checkCouple - 성공")
    @Test
    fun checkCouple_success() {
        //given
        val userId = 1L
        val coupleUserId = 2L
        val notCoupleUserId = 3L
        val user = Member(
            id = userId,
            name="김영록",
            nickname="김영록",
            email="test@test.com",
            profileImageUrl = "url",
            birthDate= LocalDate.of(2001,3,27),
            provider=Provider.KAKAO,
            role= Role.MEMBER,
            isCouple= true
        )
        val coupleUser = Member(
            id = coupleUserId,
            name="김영록 여친",
            nickname="김영록 여친",
            email="test2@test.com",
            profileImageUrl = "url2",
            birthDate= LocalDate.of(2001,5,19),
            provider=Provider.KAKAO,
            role= Role.MEMBER,
            isCouple= true
        )
        val notCoupleUser = Member(
            id = coupleUserId,
            name="김영록 여친",
            nickname="김영록 여친",
            email="test2@test.com",
            profileImageUrl = "url2",
            birthDate= LocalDate.of(2001,5,19),
            provider=Provider.KAKAO,
            role= Role.MEMBER,
            isCouple= false
        )
        `when`(memberRepository.findById(userId)).thenReturn(Optional.of(user))
        `when`(memberRepository.findById(coupleUserId)).thenReturn(Optional.of(coupleUser))
        `when`(memberRepository.findById(notCoupleUserId)).thenReturn(Optional.of(notCoupleUser))

        //when
        val resultTrue1 = coupleInfoService.checkCouple(1L)
        val resultTrue2 = coupleInfoService.checkCouple(2L)
        val resultFalse = coupleInfoService.checkCouple(3L)

        //then
        assertEquals(true, resultTrue1)
        assertEquals(true, resultTrue2)
        assertEquals(false, resultFalse)
    }

    @DisplayName("computeDday - 성공")
    @Test
    fun computeDday_success(){
        //given
        val today = LocalDate.of(2025, 4, 20)
        val yesterday = LocalDate.of(2025, 4, 19)
        val tomorrow = LocalDate.of(2025, 4, 21)

        //when
        val plusDday = coupleInfoService.computeDday(yesterday, today)
        val minusDday = coupleInfoService.computeDday(tomorrow, today)

        //then
        assertEquals(-1, minusDday)
        assertEquals(1, plusDday)
    }

    @DisplayName("getDday - 성공")
    @Test
    fun getDday_success(){
        //given
        val coupleId = 1L
        val invitorId = 1L
        val inviteeId = 2L
        val today = LocalDate.now()
        val couple = Couple(
                id = coupleId,
                invitorId = invitorId,
                inviteeId = inviteeId,
                militaryStartDate = LocalDate.of(2024,5,24),
                militaryEndDate = LocalDate.of(2025, 11, 23),
                relationshipStartDate = LocalDate.of(2024,4,24)
        )
        val militaryStartDate = couple.militaryStartDate!!
        val militaryEndDate = couple.militaryEndDate!!
        val relationshipStartDate = couple.relationshipStartDate!!
        `when`(coupleRepository.findByMemberId(invitorId)).thenReturn(couple)

        //when
        val response = coupleInfoService.getDday(invitorId)

        //then
        assertEquals(coupleInfoService.computeDday(militaryStartDate, today), response.sinceMilitaryStart)
        assertEquals(coupleInfoService.computeDday(militaryEndDate, today), response.militaryEndLeft)
        assertEquals(coupleInfoService.computeDday(relationshipStartDate, today), response.sinceLove)
    }

    @DisplayName("nickName-성공")
    @Test
    fun nickName(){
        //given
        val coupleId = 1L
        val userId = 1L
        val coupleUserId = 2L
        val couple = Couple(
            id = coupleId,
            invitorId = coupleUserId,
            inviteeId = userId,
        )
        val user = Member(
            id = 1L,
            name="김영록",
            nickname="김영록",
            email="test@test.com",
            profileImageUrl = "url",
            birthDate= LocalDate.of(2001,3,27),
            provider=Provider.KAKAO,
            role= Role.MEMBER,
            isCouple= true
        )
        val coupleUser = Member(
            id = 2L,
            name="김영록 여친",
            nickname="김영록 여친",
            email="test2@test.com",
            profileImageUrl = "url2",
            birthDate= LocalDate.of(2001,5,19),
            provider=Provider.KAKAO,
            role= Role.MEMBER,
            isCouple= true
        )
        `when`(coupleRepository.findByMemberId(userId)).thenReturn(couple)
        `when`(memberRepository.findById(userId)).thenReturn(Optional.of(user))
        `when`(memberRepository.findById(coupleUserId)).thenReturn(Optional.of(coupleUser))

        //when
        val nicknameResponse = coupleInfoService.getNickName(userId)

        //then
        assertEquals("김영록 여친", nicknameResponse.coupleNickname)
        assertEquals("김영록", nicknameResponse.userNickname)
    }

    @DisplayName("statusMessage-성공")
    @Test
    fun statusMessage(){
        //given
        val coupleId = 1L
        val userId = 1L
        val coupleUserId = 2L
        val couple = Couple(
            id = coupleId,
            invitorId = coupleUserId,
            inviteeId = userId,
        )
        val coupleUser = Member(
            id = 2L,
            name="김영록 여친",
            nickname="김영록 여친",
            email="test2@test.com",
            profileImageUrl = "url2",
            birthDate= LocalDate.of(2001,5,19),
            provider=Provider.KAKAO,
            role= Role.MEMBER,
            isCouple= true,
            statusMessage = "기분이 좋아용"
        )
        `when`(coupleRepository.findByMemberId(userId)).thenReturn(couple)
        `when`(memberRepository.findById(coupleUserId)).thenReturn(Optional.of(coupleUser))

        //when
        val statusMessage = coupleInfoService.getStatusMessage(userId)

        //then
        assertEquals("기분이 좋아용", statusMessage)
    }
}