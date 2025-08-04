package gomushin.backend.member.facade

import gomushin.backend.alarm.dto.SaveAlarmMessage
import gomushin.backend.alarm.service.StatusAlarmService
import gomushin.backend.core.CustomUserDetails
import gomushin.backend.alarm.service.NotificationRedisService
import gomushin.backend.couple.domain.service.CoupleInfoService
import gomushin.backend.member.domain.service.MemberService
import gomushin.backend.member.domain.service.NotificationService
import gomushin.backend.member.dto.request.UpdateMyBirthdayRequest
import gomushin.backend.member.dto.request.UpdateMyEmotionAndStatusMessageRequest
import gomushin.backend.member.dto.request.UpdateMyNickNameRequest
import gomushin.backend.member.dto.request.UpdateMyNotificationRequest
import gomushin.backend.member.dto.response.MyEmotionResponse
import gomushin.backend.member.dto.response.MyInfoResponse
import gomushin.backend.member.dto.response.MyNotificationResponse
import gomushin.backend.member.dto.response.MyStatusMessageResponse
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class MemberInfoFacade(
    private val memberService: MemberService,
    private val notificationService: NotificationService,
    private val statusAlarmService: StatusAlarmService,
    private val coupleInfoService: CoupleInfoService,
    private val notificationRedisService: NotificationRedisService
) {
    fun getMemberInfo(customUserDetails: CustomUserDetails): MyInfoResponse {
        val member = memberService.getById(customUserDetails.getId())
        return MyInfoResponse.of(member)
    }

    fun getMyStatusMessage(customUserDetails: CustomUserDetails): MyStatusMessageResponse {
        val member = memberService.getById(customUserDetails.getId())
        return MyStatusMessageResponse.of(member)
    }

    fun updateMyEmotionAndStatusMessage(customUserDetails: CustomUserDetails, updateMyEmotionAndStatusMessageRequest: UpdateMyEmotionAndStatusMessageRequest) {
        memberService.updateMyEmotionAndStatusMessage(customUserDetails.getId(), updateMyEmotionAndStatusMessageRequest)
        coupleInfoService.findCoupleMember(customUserDetails.getId()).also { receiver ->
            if (notificationService.getByMemberId(receiver.id).partnerStatus) {
                val sender = memberService.getById(customUserDetails.getId())
                statusAlarmService.sendStatusAlarm(sender, receiver, updateMyEmotionAndStatusMessageRequest.emotion)
            }
        }
    }

    fun getMemberEmotion(customUserDetails: CustomUserDetails): MyEmotionResponse {
        val member = memberService.getById(customUserDetails.getId())
        return MyEmotionResponse.of(member)
    }

    fun updateMyNickname(customUserDetails: CustomUserDetails, updateMyNickNameRequest: UpdateMyNickNameRequest)
        = memberService.updateMyNickname(customUserDetails.getId(), updateMyNickNameRequest)

    fun updateMyBirthDate(customUserDetails: CustomUserDetails, updateMyBirthdayRequest: UpdateMyBirthdayRequest)
        = memberService.updateMyBirthDate(customUserDetails.getId(), updateMyBirthdayRequest)

    @Transactional
    fun updateMyNotification(customUserDetails: CustomUserDetails, updateMyNotificationRequest: UpdateMyNotificationRequest) {
        notificationService.getByMemberId(customUserDetails.getId()).apply {
            updateDday(updateMyNotificationRequest.dday)
            updatePartnerStatus(updateMyNotificationRequest.partnerStatus)
        }
    }

    fun getMyNotification(customUserDetails: CustomUserDetails): MyNotificationResponse {
        val notification = notificationService.getByMemberId(customUserDetails.getId())
        return MyNotificationResponse.of(notification)
    }

    fun getMyReceivedNotification(customUserDetails: CustomUserDetails, recentDays: Long): List<SaveAlarmMessage> {
        return notificationRedisService.getAlarms(customUserDetails.getId(), recentDays)
    }
}
