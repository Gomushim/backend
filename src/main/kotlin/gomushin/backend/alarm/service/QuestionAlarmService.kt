package gomushin.backend.alarm.service

import gomushin.backend.alarm.util.MessageParsingUtil
import gomushin.backend.core.infrastructure.exception.BadRequestException
import gomushin.backend.member.domain.service.MemberService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class QuestionAlarmService (
    private val fcmService: FCMService,
    private val memberService: MemberService
) {
    private val log: Logger = LoggerFactory.getLogger(QuestionAlarmService::class.java)
    private val questionMessages = listOf(
        "오늘 하루는 어땠나요?+연인에게도 안부를 전해보세요",
        "사랑은 매일 채우는 것+오늘도 연인과 한 조각 채워보세요",
        "네가 있어서 참 좋아+연인에게 고마움을 전해보세요",
        "매일 사랑이 자라요+가벼운 전화통화 어때요?"
    )

    @Scheduled(cron = "0 0 19 * * *", zone = "Asia/Seoul")
    fun sendQuestionAlarms() {
        val coupleMembers = memberService.getAllCoupledMemberWithEnabledNotification()
        runBlocking {
            coupleMembers.map { member ->
                async(Dispatchers.IO) {
                    try {
                        val notificationContent = questionMessages.random()
                        val (title, sendContent) = MessageParsingUtil.parse(notificationContent)
                        log.info("질문형 메시지 전송 : 수신자 {${member.id}}, 제목 {${title}}, 내용{${sendContent}}, 전송시각{${LocalDateTime.now()}}\n")
                        fcmService.sendMessageTo(member.fcmToken, title, sendContent)
                    } catch (e: Exception) {
                        log.error("질문형 메시지 전송오류 : 수신자 {${member.name}}, 전송시각{${LocalDateTime.now()}}\n")
                    }
                }
            }.awaitAll()
        }
    }
}