package gomushin.backend.member.domain.service

import gomushin.backend.core.service.S3Service
import gomushin.backend.couple.domain.service.AnniversaryService
import gomushin.backend.couple.domain.service.CoupleService
import gomushin.backend.schedule.domain.service.CommentService
import gomushin.backend.schedule.domain.service.LetterService
import gomushin.backend.schedule.domain.service.PictureService
import gomushin.backend.schedule.domain.service.ScheduleService
import org.springframework.stereotype.Service

@Service
class LeaveService(
    private val anniversaryService: AnniversaryService,
    private val commentService: CommentService,
    private val coupleService: CoupleService,
    private val letterService: LetterService,
    private val memberService: MemberService,
    private val notificationService: NotificationService,
    private val pictureService: PictureService,
    private val scheduleService: ScheduleService,
    private val s3Service: S3Service,
) {
    fun leave(memberId: Long, coupleId : Long) {
        anniversaryService.deleteAllByCoupleId(coupleId)
        commentService.deleteAllByMemberId(memberId)
        coupleService.deleteByMemberId(memberId)
        notificationService.deleteAllByMember(memberId)
        scheduleService.deleteAllByMember(memberId)

        val letters = letterService.findAllByAuthorId(memberId)
        pictureService.findAllByLetterIds(letters)
            .takeIf { it.isNotEmpty() }
            ?.forEach { picture ->
                s3Service.deleteFile(picture.pictureUrl)
            }

        pictureService.deleteAllByLetterIds(letters)
        letterService.deleteAllByMemberId(memberId)
        memberService.deleteMember(memberId)
    }
}