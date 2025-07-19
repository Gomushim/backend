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
        deleteComments(memberId, partner.id)
        coupleService.deleteByMemberId(memberId)
        deleteNotifications(memberId, partner.id)
        deleteSchedules(memberId, partner.id)

        val memberLetters = letterService.findAllByAuthorId(memberId)
        val partnerLetters = letterService.findAllByAuthorId(partner.id)
        val pictureUrlsToDelete = mutableListOf<String>()

        pictureService.findAllByLetterIds(memberLetters)
            .takeIf { it.isNotEmpty() }
            ?.forEach { picture -> pictureUrlsToDelete.add(picture.pictureUrl) }
        pictureService.findAllByLetterIds(partnerLetters)
            .takeIf { it.isNotEmpty() }
            ?.forEach { picture -> pictureUrlsToDelete.add(picture.pictureUrl) }

        deleteAllPictures(memberLetters, partnerLetters)
        deleteAllLetters(memberId, partner.id)

        clearCoupleStatusMessages(memberId, partner.id)

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

    private fun deleteComments(memberId: Long, partnerId: Long) {
        commentService.deleteAllByMemberId(memberId)
        commentService.deleteAllByMemberId(partnerId)
    }

    private fun deleteNotifications(memberId: Long, partnerId: Long) {
        notificationService.deleteAllByMember(memberId)
        notificationService.deleteAllByMember(partnerId)
    }

    private fun deleteSchedules(memberId: Long, partnerId: Long) {
        scheduleService.deleteAllByMemberId(memberId)
        scheduleService.deleteAllByMemberId(partnerId)
    }

    private fun deleteAllPictures(memberLetterIds: List<Long>, partnerLetterIds: List<Long>) {
        pictureService.deleteAllByLetterIds(memberLetterIds)
        pictureService.deleteAllByLetterIds(partnerLetterIds)
    }

    private fun deleteAllLetters(memberId: Long, partnerId: Long) {
        letterService.deleteAllByMemberId(memberId)
        letterService.deleteAllByMemberId(partnerId)
    }

    private fun clearCoupleStatusMessages(memberId: Long, partnerId: Long) {
        memberService.clearMemberStatusMessage(memberId)
        memberService.clearMemberStatusMessage(partnerId)
    }
}

