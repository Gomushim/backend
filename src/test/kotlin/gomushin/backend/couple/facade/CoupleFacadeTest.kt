package gomushin.backend.couple.facade

import gomushin.backend.core.CustomUserDetails
import gomushin.backend.couple.domain.entity.Couple
import gomushin.backend.couple.domain.service.AnniversaryService
import gomushin.backend.couple.domain.service.CoupleConnectService
import gomushin.backend.couple.domain.service.CoupleInfoService
import gomushin.backend.couple.domain.service.CoupleService
import gomushin.backend.couple.domain.value.AnniversaryEmoji
import gomushin.backend.couple.dto.request.GenerateAnniversaryRequest
import gomushin.backend.couple.dto.request.UpdateMilitaryDateRequest
import gomushin.backend.couple.dto.request.UpdateRelationshipStartDateRequest
import gomushin.backend.couple.dto.response.DdayResponse
import gomushin.backend.couple.dto.response.NicknameResponse
import gomushin.backend.member.domain.entity.Member
import gomushin.backend.member.domain.service.MemberService
import gomushin.backend.member.domain.value.Emotion
import gomushin.backend.member.domain.value.Provider
import gomushin.backend.member.domain.value.Role
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDate
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
class CoupleFacadeTest {
    @Mock
    private lateinit var coupleInfoService: CoupleInfoService

    @Mock
    private lateinit var coupleConnectService: CoupleConnectService

    @Mock
    private lateinit var anniversaryService: AnniversaryService

    @Mock
    private lateinit var coupleService: CoupleService

    @Mock
    private lateinit var memberService: MemberService

    @InjectMocks
    private lateinit var coupleFacade: CoupleFacade

    private lateinit var customUserDetails: CustomUserDetails
    private lateinit var member1: Member
    private lateinit var member2: Member
    private lateinit var couple : Couple

    @BeforeEach
    fun setUp(){
        member1 = Member(
                id = 1L,
                name = "곰신",
                nickname = "곰신닉네임",
                email = "test1@test.com",
                birthDate = null,
                profileImageUrl = null,
                provider = Provider.KAKAO,
                role = Role.MEMBER,
        )

        member2 = Member(
                id = 2L,
                name = "꽃신",
                nickname = "꽃신닉네임",
                email = "test2@test.com",
                birthDate = null,
                profileImageUrl = null,
                provider = Provider.KAKAO,
                role = Role.MEMBER,
        )

        couple = Couple(
                id = 1L,
                invitorId = 1L,
                inviteeId = 2L,
                militaryStartDate = LocalDate.of(2021,5,24)
        )

        customUserDetails = Mockito.mock(CustomUserDetails::class.java)

    }

    @DisplayName("grade 조회")
    @Test
    fun getGradeInfo(){
        `when`(customUserDetails.getId()).thenReturn(1L)
        `when`(coupleInfoService.getGrade(customUserDetails.getId())).thenReturn(1)
        val result = coupleFacade.getGradeInfo(customUserDetails)
        verify(coupleInfoService).getGrade(1L)
        assertEquals(1, result.grade)
    }

    @DisplayName("커플 연동 여부 조회")
    @Test
    fun checkConnect(){
        `when`(customUserDetails.getId()).thenReturn(1L)
        `when`(coupleInfoService.checkCouple(customUserDetails.getId())).thenReturn(true)
        val result = coupleFacade.checkConnect(customUserDetails)
        verify(coupleInfoService).checkCouple(1L)
        assertEquals(true, result)
    }

    @DisplayName("디데이 조회 - 정상응답")
    @Test
    fun getDday(){
        `when`(customUserDetails.getId()).thenReturn(1L)
        `when`(coupleInfoService.getDday(customUserDetails.getId())).thenReturn(DdayResponse(100, 200, -345))
        val result = coupleFacade.getDday(customUserDetails)
        verify(coupleInfoService).getDday(1L)
        assertEquals(100, result.sinceLove)
        assertEquals(200, result.sinceMilitaryStart)
        assertEquals(-345, result.militaryEndLeft)
    }

    @DisplayName("디데이 조회 - 날짜 정보 안 들어갔을 때")
    @Test
    fun getDdayNull(){
        `when`(customUserDetails.getId()).thenReturn(1L)
        `when`(coupleInfoService.getDday(customUserDetails.getId())).thenReturn(DdayResponse(null, null, null))
        val result = coupleFacade.getDday(customUserDetails)
        verify(coupleInfoService).getDday(1L)
        assertEquals(null, result.sinceLove)
        assertEquals(null, result.sinceMilitaryStart)
        assertEquals(null, result.militaryEndLeft)
    }

    @DisplayName("닉네임 조회 - 정상응답")
    @Test
    fun nickName(){
        `when`(customUserDetails.getId()).thenReturn(1L)
        `when`(coupleInfoService.getNickName(customUserDetails.getId())).thenReturn(NicknameResponse("김영록", "김영록 여친"))
        val result = coupleFacade.nickName(customUserDetails)
        verify(coupleInfoService).getNickName(1L)
        assertEquals("김영록", result.userNickname)
        assertEquals("김영록 여친", result.coupleNickname)
    }

    @DisplayName("상태 메시지 조회 - 정상응답")
    @Test
    fun statusMessage(){
        `when`(customUserDetails.getId()).thenReturn(1L)
        `when`(coupleInfoService.getStatusMessage(customUserDetails.getId())).thenReturn("기분이 좋아용")
        val result = coupleFacade.statusMessage(customUserDetails)
        verify(coupleInfoService).getStatusMessage(1L)
        assertEquals("기분이 좋아용", result.statusMessage)
    }

    @DisplayName("입대일, 전역일 수정 - 정상응답")
    @Test
    fun updateMilitaryDate() {
        `when`(customUserDetails.getId()).thenReturn(1L)
        val updateMilitaryDateRequest = UpdateMilitaryDateRequest(
            LocalDate.of(2022, 5, 24),
            LocalDate.of(2023,11,23)
        )
        val result = coupleFacade.updateMilitaryDate(customUserDetails, updateMilitaryDateRequest)
        verify(coupleInfoService).updateMilitaryDate(customUserDetails.getCouple(), updateMilitaryDateRequest)
    }

    @DisplayName("만난날 수정 - 정상응답")
    @Test
    fun updateRelationshipStartDate() {
        `when`(customUserDetails.getId()).thenReturn(1L)
        val updateRelationshipStartDateRequest = UpdateRelationshipStartDateRequest(
            LocalDate.of(2022, 5, 24),
        )
        val result = coupleFacade.updateRelationshipStartDate(customUserDetails, updateRelationshipStartDateRequest)
        verify(coupleInfoService).updateRelationshipStartDate(customUserDetails.getCouple(), updateRelationshipStartDateRequest)
    }

    @DisplayName("이모지 조회 - 정상응답")
    @Test
    fun getEmotion() {
        `when`(customUserDetails.getId()).thenReturn(1L)
        `when`(coupleInfoService.getCoupleEmotion(customUserDetails.getId())).thenReturn(Emotion.HAPPY)
        val result = coupleFacade.getCoupleEmotion(customUserDetails)
        verify(coupleInfoService).getCoupleEmotion(1L)
        assertEquals(Emotion.HAPPY.name, result.emotion)
    }

    @DisplayName("기념일 생성 - 정상응답")
    @Test
    fun generateAnniversary() {
        //given
        val generateAnniversaryRequest = GenerateAnniversaryRequest(
            "전역일",
            AnniversaryEmoji.CAKE,
            LocalDate.of(2025, 5, 1)
        )
        `when`(customUserDetails.getCouple()).thenReturn(couple)
//        `when`(anniversaryService.generateAnniversary(customUserDetails.getCouple(),generateAnniversaryRequest)).thenReturn(
//            Mockito.mock(Unit::class.java)
//        )
        //when
        coupleFacade.generateAnniversary(customUserDetails, generateAnniversaryRequest)
        //then
        verify(anniversaryService).generateAnniversary(couple, generateAnniversaryRequest)
    }
}
