package gomushin.backend.member.domain.service

import gomushin.backend.member.domain.entity.Member
import gomushin.backend.member.domain.repository.MemberRepository
import gomushin.backend.member.domain.value.Provider
import gomushin.backend.member.domain.value.Role
import gomushin.backend.member.dto.request.UpdateMyBirthdayRequest
import gomushin.backend.member.dto.request.UpdateMyEmotionAndStatusMessageRequest
import gomushin.backend.member.dto.request.UpdateMyNickNameRequest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDate
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

    @DisplayName("이모지 및 상태 메시지 업데이트 - 성공")
    @Test
    fun updateMyEmotionAndStatusMessage() {
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
            emotion = 1,
            statusMessage = "상태 변경전"
        )
        val updateMyEmotionAndStatusMessageRequest = UpdateMyEmotionAndStatusMessageRequest(2, "상태 변경후")
        //when
        `when`(memberRepository.findById(memberId)).thenReturn(Optional.of(expectedMember))
        val result = memberService.updateMyEmotionAndStatusMessage(memberId, updateMyEmotionAndStatusMessageRequest)
        //then
        assertEquals(expectedMember.emotion, updateMyEmotionAndStatusMessageRequest.emotion)
        assertEquals(expectedMember.statusMessage, updateMyEmotionAndStatusMessageRequest.statusMessage)
    }

    @DisplayName("닉네임 수정 - 성공")
    @Test
    fun updateMyNickname() {
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
            emotion = 1,
            statusMessage = "상태 변경전"
        )
        val updateMyNickNameRequest = UpdateMyNickNameRequest("테스트 닉네임 수정")
        //when
        `when`(memberRepository.findById(memberId)).thenReturn(Optional.of(expectedMember))
        val result = memberService.updateMyNickname(memberId, updateMyNickNameRequest)
        //then
        assertEquals(expectedMember.nickname, updateMyNickNameRequest.nickname)
    }

    @DisplayName("생년월일 수정 - 성공")
    @Test
    fun updateMyBirthDate() {
        // given
        val memberId = 1L
        val expectedMember = Member(
            id = 1L,
            name = "테스트",
            nickname = "테스트 닉네임",
            email = "test@test.com",
            birthDate = LocalDate.of(2001, 3, 27),
            profileImageUrl = null,
            provider = Provider.KAKAO,
            role = Role.GUEST,
            emotion = 1,
            statusMessage = "상태 변경전",
        )
        val updateMyBirthdayRequest = UpdateMyBirthdayRequest(LocalDate.of(2001, 3, 30))
        //when
        `when`(memberRepository.findById(memberId)).thenReturn(Optional.of(expectedMember))
        val result = memberService.updateMyBirthDate(memberId, updateMyBirthdayRequest)
        //then
        assertEquals(expectedMember.birthDate, updateMyBirthdayRequest.birthDate)
    }
}
