package gomushin.backend.member.facade

import gomushin.backend.core.CustomUserDetails
import gomushin.backend.member.domain.entity.Member
import gomushin.backend.member.domain.service.MemberService
import gomushin.backend.member.domain.value.Provider
import gomushin.backend.member.domain.value.Role
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
class MemberInfoFacadeTest {
    @Mock
    private lateinit var memberService: MemberService

    @InjectMocks
    private lateinit var memberInfoFacade: MemberInfoFacade

    private lateinit var customUserDetails: CustomUserDetails
    private lateinit var member: Member

    @BeforeEach
    fun setUp() {
        member = Member(
            id = 1L,
            name = "테스트",
            nickname = "테스트 닉네임",
            email = "test@test.com",
            birthDate = null,
            profileImageUrl = null,
            provider = Provider.KAKAO,
            role = Role.MEMBER,
            statusMessage = "상태 메시지"
        )

        customUserDetails = mock(CustomUserDetails::class.java)

        `when`(customUserDetails.getId()).thenReturn(1L)

    }

    @DisplayName("내 정보 조회 - [GUEST]")
    @Test
    fun getMyInfo() {
        // given
        `when`(memberService.getById(customUserDetails.getId())).thenReturn(member)
        // when
        val result = memberInfoFacade.getMemberInfo(customUserDetails)
        // then
        verify(memberService).getById(1L)
        assertEquals(member.nickname, result.nickname)
    }

    @DisplayName("내 상태 메시지 조회")
    @Test
    fun getMyStatusMessage() {
        //given
        `when`(memberService.getById(customUserDetails.getId())).thenReturn(member)
        //when
        val result = memberInfoFacade.getMyStatusMessage(customUserDetails)
        //then
        verify(memberService).getById(1L)
        assertEquals(member.statusMessage, result.statusMessage)
    }
}
