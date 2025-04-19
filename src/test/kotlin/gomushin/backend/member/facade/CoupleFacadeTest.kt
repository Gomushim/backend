package gomushin.backend.member.facade

import gomushin.backend.core.CustomUserDetails
import gomushin.backend.couple.domain.entity.Couple
import gomushin.backend.couple.domain.service.CoupleConnectService
import gomushin.backend.couple.domain.service.CoupleInfoService
import gomushin.backend.couple.facade.CoupleFacade
import gomushin.backend.member.domain.entity.Member
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

        `when`(customUserDetails.getId()).thenReturn(1L)
    }

    @DisplayName("grade 조회")
    @Test
    fun getGradeInfo(){
        `when`(coupleInfoService.getGrade(customUserDetails.getId())).thenReturn(1)
        val result = coupleFacade.getGradeInfo(customUserDetails)
        verify(coupleInfoService).getGrade(1L)
        assertEquals(1, result.grade)
    }

    @DisplayName("커플 연동 여부 조회")
    @Test
    fun checkConnect(){
        `when`(coupleInfoService.checkCouple(customUserDetails.getId())).thenReturn(true)
        val result = coupleFacade.checkConnect(customUserDetails)
        verify(coupleInfoService).checkCouple(1L)
        assertEquals(true, result)
    }

}