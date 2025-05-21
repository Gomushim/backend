package gomushin.backend.member.facade

import gomushin.backend.core.CustomUserDetails
import gomushin.backend.core.service.S3Service
import gomushin.backend.couple.domain.service.AnniversaryService
import gomushin.backend.couple.domain.service.CoupleInfoService
import gomushin.backend.couple.domain.service.CoupleService
import gomushin.backend.member.domain.service.MemberService
import gomushin.backend.member.domain.service.NotificationService
import gomushin.backend.schedule.domain.service.CommentService
import gomushin.backend.schedule.domain.service.LetterService
import gomushin.backend.schedule.domain.service.PictureService
import gomushin.backend.schedule.domain.service.ScheduleService
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
    private val s3Service: S3Service,
    private val coupleInfoService: CoupleInfoService
) {
    @Transactional
    fun leave(customUserDetails: CustomUserDetails) {
        val memberId = customUserDetails.getId()
        val coupleId = customUserDetails.getCouple().id
        val partner = coupleInfoService.findCoupleMember(memberId)
        anniversaryService.deleteAllByCoupleId(coupleId)
        commentService.deleteAllByMemberId(memberId)
        coupleService.deleteByMemberId(memberId)
        notificationService.deleteAllByMember(memberId)
        scheduleService.deleteAllByMemberId(memberId)

        val letters = letterService.findAllByAuthorId(memberId)
        pictureService.findAllByLetterIds(letters)
            .takeIf { it.isNotEmpty() }
            ?.forEach { picture ->
                s3Service.deleteFile(picture.pictureUrl)
            }

        pictureService.deleteAllByLetterIds(letters)
        letterService.deleteAllByMemberId(memberId)
        memberService.deleteMember(memberId)

        partner.updateIsCouple(false)
    }
}
