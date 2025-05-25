package gomushin.backend.member.facade

import gomushin.backend.core.CustomUserDetails
import gomushin.backend.core.event.dto.S3DeleteEvent
import gomushin.backend.couple.domain.service.AnniversaryService
import gomushin.backend.couple.domain.service.CoupleInfoService
import gomushin.backend.couple.domain.service.CoupleService
import gomushin.backend.member.domain.service.MemberService
import gomushin.backend.member.domain.service.NotificationService
import gomushin.backend.schedule.domain.service.CommentService
import gomushin.backend.schedule.domain.service.LetterService
import gomushin.backend.schedule.domain.service.PictureService
import gomushin.backend.schedule.domain.service.ScheduleService
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class LeaveFacade(
    private val anniversaryService: AnniversaryService,
    private val commentService: CommentService,
    private val coupleService: CoupleService,
    private val letterService: LetterService,
    private val memberService: MemberService,
    private val notificationService: NotificationService,
    private val pictureService: PictureService,
    private val scheduleService: ScheduleService,
    private val coupleInfoService: CoupleInfoService,
    private val applicationEventPublisher: ApplicationEventPublisher,
) {
    @Transactional
    fun leave(customUserDetails: CustomUserDetails) {
        val memberId = customUserDetails.getId()
        val coupleId = customUserDetails.getCouple().id
        val partner = coupleInfoService.findCoupleMember(memberId)
        anniversaryService.deleteAllByCoupleId(coupleId)
        commentService.deleteAllByMemberId(memberId)
        commentService.deleteAllByMemberId(partner.id)
        coupleService.deleteByMemberId(memberId)
        notificationService.deleteAllByMember(memberId)
        notificationService.deleteAllByMember(partner.id)
        scheduleService.deleteAllByMemberId(memberId)
        scheduleService.deleteAllByMemberId(partner.id)

        val memberLetters = letterService.findAllByAuthorId(memberId)
        val partnerLetters = letterService.findAllByAuthorId(partner.id)

        val pictureUrlsToDelete = mutableListOf<String>()

        pictureService.findAllByLetterIds(memberLetters)
            .takeIf { it.isNotEmpty() }
            ?.forEach { picture -> pictureUrlsToDelete.add(picture.pictureUrl) }
        pictureService.findAllByLetterIds(partnerLetters)
            .takeIf { it.isNotEmpty() }
            ?.forEach { picture -> pictureUrlsToDelete.add(picture.pictureUrl) }

        pictureService.deleteAllByLetterIds(memberLetters)
        pictureService.deleteAllByLetterIds(partnerLetters)

        letterService.deleteAllByMemberId(memberId)
        letterService.deleteAllByMemberId(partner.id)

        partner.updateIsCouple(false)

        memberService.deleteMember(memberId)
        if (pictureUrlsToDelete.isNotEmpty()) {
            applicationEventPublisher.publishEvent(
                S3DeleteEvent(
                    pictureUrls = pictureUrlsToDelete
                )
            )
        }
    }
}

