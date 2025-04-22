package gomushin.backend.member.domain.service

import gomushin.backend.member.domain.entity.Member
import gomushin.backend.member.domain.repository.MemberRepository
import gomushin.backend.member.domain.value.Provider
import gomushin.backend.member.domain.value.Role
import gomushin.backend.member.dto.request.OnboardingRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDate
import java.util.*
import kotlin.test.Test

@ExtendWith(MockitoExtension::class)
class OnboardingServiceTest {
    @Mock
    private lateinit var memberRepository: MemberRepository

    private val onboardingService: OnboardingService by lazy {
        OnboardingService(memberRepository)
    }

    @Test
    fun `onboarding 성공 케이스`() {
        // given
        val memberId = 1L
        val existingMember = Member(
            id = memberId,
            name = "테스트",
            nickname = "원래 닉네임",
            email = "test@example.com",
            birthDate = LocalDate.of(1990, 1, 1),
            profileImageUrl = null,
            provider = Provider.KAKAO,
            role = Role.GUEST,
        )

        val onboardingRequest = OnboardingRequest(
            nickname = "새로운 닉네임",
            birthDate = LocalDate.of(2000, 1, 1),
            fcmToken = "fcmToken",
            isNotification = false,
        )

        `when`(memberRepository.findById(memberId)).thenReturn(Optional.of(existingMember))

        // when
        onboardingService.onboarding(memberId, onboardingRequest)

        // then
        assertEquals("새로운 닉네임", existingMember.nickname)
        assertEquals(LocalDate.of(2000, 1, 1), existingMember.birthDate)
        assertEquals(Role.MEMBER, existingMember.role)

        verify(memberRepository).findById(memberId)
    }
}
