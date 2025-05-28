package gomushin.backend.alarm.facade

import gomushin.backend.alarm.service.FCMService
import gomushin.backend.alarm.service.NotificationRedisService
import gomushin.backend.alarm.value.RedirectURL
import gomushin.backend.couple.domain.service.AnniversaryService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class DdayAlarmFacade(
    private val fcmService: FCMService,
    private val anniversaryService: AnniversaryService,
    private val notificationRedisService: NotificationRedisService,
    private val redirectURL: RedirectURL
) {
    private val log: Logger = LoggerFactory.getLogger(QuestionAlarmFacade::class.java)
    private val alarmTitle = "오늘의 디데이가 도착했어요"
    
    @Scheduled(cron = "\${scheduling.cron.d-day}", zone = "\${scheduling.zone.seoul}")
    fun sendDdayAlarms() {
        val sendDdayAlarmContents = anniversaryService.getTodayAnniversaryMemberFcmTokens(LocalDate.now())
        log.info("전송 크기 : ${sendDdayAlarmContents.size}")
        runBlocking {
            sendDdayAlarmContents.map { content ->
                async(Dispatchers.IO) {
                    try {
                        log.info("디데이 메시지 전송 : 수신자 {${content.memberId}}, 제목 {${alarmTitle}}, 내용{${content.title}}, 전송시각{${LocalDateTime.now()}}\n")
                        notificationRedisService.saveAlarm(alarmTitle, content.title, content.memberId)
                        fcmService.sendMessageTo(content.fcmToken, alarmTitle, content.title, redirectURL.dday)
                    } catch (e: Exception) {
                        log.error("디데이 메시지 전송오류 : 수신자 {${content.memberId}}, 전송시각{${LocalDateTime.now()}}\n")
                    }
                }
            }.awaitAll()
        }
    }
}