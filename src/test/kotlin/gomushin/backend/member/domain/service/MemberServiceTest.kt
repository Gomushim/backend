package gomushin.backend.member.domain.service

import gomushin.backend.member.domain.entity.Member
import gomushin.backend.member.domain.repository.MemberRepository
import gomushin.backend.member.domain.value.Provider
import gomushin.backend.member.domain.value.Role
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
class MemberServiceTest {

    @Mock
    private lateinit var memberRepository: MemberRepository

    @InjectMocks
    private lateinit var memberService: MemberService

    @DisplayName("내 정보 조회 - [GUEST]")
    @Test
    fun getGuestInfo_success() {
        // given
        val memberId = 1L
        val expectedMember = Member(
            id = 1L,
            name = "테스트",
            nickname = "테스트 닉네임",
            email = "test@test.com",
            birthDate = null,
            profileImageUrl = null,
            provider = Provider.KAKAO,
            role = Role.GUEST,
        )

        // when
        `when`(memberRepository.findById(memberId)).thenReturn(Optional.of(expectedMember))
        val result = memberService.getById(memberId)

        // then
        assertEquals(expectedMember, result)
    }
}
