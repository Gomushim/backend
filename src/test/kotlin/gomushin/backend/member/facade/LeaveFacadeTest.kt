package gomushin.backend.member.facade

import gomushin.backend.core.CustomUserDetails
import gomushin.backend.core.event.dto.S3DeleteEvent
import gomushin.backend.couple.domain.entity.Couple
import gomushin.backend.couple.domain.service.AnniversaryService
import gomushin.backend.couple.domain.service.CoupleInfoService
import gomushin.backend.couple.domain.service.CoupleService
import gomushin.backend.member.domain.entity.Member
import gomushin.backend.member.domain.service.MemberService
import gomushin.backend.member.domain.service.NotificationService
import gomushin.backend.schedule.domain.entity.Picture
import gomushin.backend.schedule.domain.service.CommentService
import gomushin.backend.schedule.domain.service.LetterService
import gomushin.backend.schedule.domain.service.PictureService
import gomushin.backend.schedule.domain.service.ScheduleService
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.context.ApplicationEventPublisher

@ExtendWith(MockKExtension::class)
class LeaveFacadeTest {
    @MockK
    lateinit var anniversaryService: AnniversaryService
    @MockK
    lateinit var commentService: CommentService
    @MockK
    lateinit var coupleService: CoupleService
    @MockK
    lateinit var letterService: LetterService
    @MockK
    lateinit var memberService: MemberService
    @MockK
    lateinit var notificationService: NotificationService
    @MockK
    lateinit var pictureService: PictureService
    @MockK
    lateinit var scheduleService: ScheduleService
    @MockK
    lateinit var coupleInfoService: CoupleInfoService
    @MockK
    lateinit var applicationEventPublisher: ApplicationEventPublisher

    @InjectMockKs
    lateinit var leaveFacade: LeaveFacade

    private val customUserDetails = mockk<CustomUserDetails>()
    private val couple = mockk<Couple>()
    private val partner = mockk<Member>()

    @BeforeEach
    fun setUp() {
        every { customUserDetails.getId() } returns 1L
        every { customUserDetails.getCouple() } returns couple
        every { couple.id } returns 100L
        every { partner.id } returns 2L
        every { partner.updateIsCouple(false) } just Runs
    }

    @DisplayName("leave 메서드는 회원 탈퇴를 성공적으로 처리한다.")
    @Test
    fun leave_success() {
        // given
        val memberId = 1L
        val partnerId = 2L
        val coupleId = 100L
        val memberLetterIds = listOf(10L, 11L)
        val partnerLetterIds = listOf(20L, 21L)
        val pictures = listOf(
            mockk<Picture> { every { pictureUrl } returns "url1" },
            mockk<Picture> { every { pictureUrl } returns "url2" }
        )

        every { coupleInfoService.findCoupleMember(memberId) } returns partner
        every { anniversaryService.deleteAllByCoupleId(coupleId) } just Runs
        every { commentService.deleteAllByMemberId(memberId) } just Runs
        every { commentService.deleteAllByMemberId(partnerId) } just Runs
        every { coupleService.deleteByMemberId(memberId) } just Runs
        every { notificationService.deleteAllByMember(memberId) } just Runs
        every { notificationService.deleteAllByMember(partnerId) } just Runs
        every { scheduleService.deleteAllByMemberId(memberId) } just Runs
        every { scheduleService.deleteAllByMemberId(partnerId) } just Runs
        every { letterService.findAllByAuthorId(memberId) } returns memberLetterIds
        every { letterService.findAllByAuthorId(partnerId) } returns partnerLetterIds
        every { pictureService.findAllByLetterIds(memberLetterIds) } returns pictures
        every { pictureService.findAllByLetterIds(partnerLetterIds) } returns emptyList()
        every { pictureService.deleteAllByLetterIds(memberLetterIds) } just Runs
        every { pictureService.deleteAllByLetterIds(partnerLetterIds) } just Runs
        every { letterService.deleteAllByMemberId(memberId) } just Runs
        every { letterService.deleteAllByMemberId(partnerId) } just Runs
        every { memberService.clearMemberStatusMessage(memberId) } just Runs
        every { memberService.clearMemberStatusMessage(partnerId) } just Runs
        every { memberService.deleteMember(memberId) } just Runs
        every { applicationEventPublisher.publishEvent(any<S3DeleteEvent>()) } just Runs

        // when
        leaveFacade.leave(customUserDetails)

        // then
        verify(exactly = 1) { coupleInfoService.findCoupleMember(memberId) }
        verify(exactly = 1) { anniversaryService.deleteAllByCoupleId(coupleId) }
        verify(exactly = 1) { commentService.deleteAllByMemberId(memberId) }
        verify(exactly = 1) { commentService.deleteAllByMemberId(partnerId) }
        verify(exactly = 1) { coupleService.deleteByMemberId(memberId) }
        verify(exactly = 1) { notificationService.deleteAllByMember(memberId) }
        verify(exactly = 1) { notificationService.deleteAllByMember(partnerId) }
        verify(exactly = 1) { scheduleService.deleteAllByMemberId(memberId) }
        verify(exactly = 1) { scheduleService.deleteAllByMemberId(partnerId) }
        verify(exactly = 1) { letterService.findAllByAuthorId(memberId) }
        verify(exactly = 1) { letterService.findAllByAuthorId(partnerId) }
        verify(exactly = 1) { pictureService.findAllByLetterIds(memberLetterIds) }
        verify(exactly = 1) { pictureService.findAllByLetterIds(partnerLetterIds) }
        verify(exactly = 1) { pictureService.deleteAllByLetterIds(memberLetterIds) }
        verify(exactly = 1) { pictureService.deleteAllByLetterIds(partnerLetterIds) }
        verify(exactly = 1) { letterService.deleteAllByMemberId(memberId) }
        verify(exactly = 1) { letterService.deleteAllByMemberId(partnerId) }
        verify(exactly = 1) { memberService.clearMemberStatusMessage(memberId) }
        verify(exactly = 1) { memberService.clearMemberStatusMessage(partnerId) }
        verify(exactly = 1) { partner.updateIsCouple(false) }
        verify(exactly = 1) { memberService.deleteMember(memberId) }
        verify(exactly = 1) { applicationEventPublisher.publishEvent(any<S3DeleteEvent>()) }
    }

    @DisplayName("사진이 없는 경우 S3DeleteEvent가 발생하지 않는다.")
    @Test
    fun leave_success_without_pictures() {
        // given
        val memberId = 1L
        val partnerId = 2L
        val coupleId = 100L
        val memberLetterIds = listOf(10L, 11L)
        val partnerLetterIds = listOf(20L, 21L)

        every { coupleInfoService.findCoupleMember(memberId) } returns partner
        every { anniversaryService.deleteAllByCoupleId(coupleId) } just Runs
        every { commentService.deleteAllByMemberId(any()) } just Runs
        every { coupleService.deleteByMemberId(memberId) } just Runs
        every { notificationService.deleteAllByMember(any()) } just Runs
        every { scheduleService.deleteAllByMemberId(any()) } just Runs
        every { letterService.findAllByAuthorId(memberId) } returns memberLetterIds
        every { letterService.findAllByAuthorId(partnerId) } returns partnerLetterIds
        every { pictureService.findAllByLetterIds(memberLetterIds) } returns emptyList()
        every { pictureService.findAllByLetterIds(partnerLetterIds) } returns emptyList()
        every { pictureService.deleteAllByLetterIds(any()) } just Runs
        every { letterService.deleteAllByMemberId(any()) } just Runs
        every { memberService.clearMemberStatusMessage(any()) } just Runs
        every { memberService.deleteMember(memberId) } just Runs

        // when
        leaveFacade.leave(customUserDetails)

        // then
        verify(exactly = 0) { applicationEventPublisher.publishEvent(any<S3DeleteEvent>()) }
        verify(exactly = 1) { memberService.deleteMember(memberId) }
    }
}
