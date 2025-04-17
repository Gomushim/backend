package gomushin.backend.member.facade

import gomushin.backend.core.CustomUserDetails
import gomushin.backend.member.domain.entity.Member
import gomushin.backend.member.domain.service.MemberInfoService
import gomushin.backend.member.domain.value.Provider
import gomushin.backend.member.domain.value.Role
import gomushin.backend.member.dto.response.MyInfoResponse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
class MemberInfoFacadeTest {
    @Mock
    private lateinit var memberInfoService: MemberInfoService

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
            role = Role.GUEST,
        )

        val authorities = mutableListOf<GrantedAuthority>(SimpleGrantedAuthority("ROLE_GUEST"))
        customUserDetails = mock(CustomUserDetails::class.java)

        `when`(customUserDetails.getId()).thenReturn(1L)
        `when`(customUserDetails.authorities).thenReturn(authorities)

    }

    @DisplayName("내 정보 조회 - [GUEST]")
    @Test
    fun getMyInfo() {
        // given
        `when`(memberInfoService.getById(customUserDetails.getId())).thenReturn(member)
        // when
        val result = memberInfoFacade.getMemberInfo(customUserDetails)
        // then
        verify(memberInfoService).getById(1L)
        assertEquals(member.nickname, (result as MyInfoResponse).nickname)
    }
}
