package gomushin.backend.alarm.service

import gomushin.backend.member.domain.entity.Member
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class StatusAlarmService (
    private val fcmService: FCMService
) {
    private val dummyList = listOf<String>()
    private val emojiMiss = listOf(
        "보고싶다는 마음이 도착했어요.+오늘은 짧은 안부라도 건네볼까요?",
        "보고 싶다고 표현하는 건 용기예요.+작은 마음이 큰 위로가 될 거예요")
    private val emojiTired = listOf(
        "오늘 많이 힘들었나봐요.+따뜻한 말 한마디가 큰 힘이 돼요",
        "오늘 피곤한 날이래요.+연인에게 따뜻한 응원 어때요?"
    )
    private val emojiSad = listOf(
        "서운한 마음이 들었대요.+연인과 진심 어린 대화 어때요?",
        "연인의 마음이 무거운가봐요.+따뜻한 공감이 필요해요"
    )
    private val emojiGood = listOf(
        "기분 좋은 하루를 함께 나눠보세요.+당신의 행복이 전해질 거예요.",
        "좋은 일이 있었대요!+축하해주고 같이 기뻐해주세요\uD83D\uDC9B"
    )
    private val emojiAnnoy = listOf(
        "연인이 짜증나는 일이 있었대요.+들어주는 것도 큰 위로가 될거예요",
        "OO님 오늘 좀 힘들었나 봐요.+오늘 전화통화 어때요?"
    )
    private val emojiWorry = listOf(
        "당신을 걱정하는 마음이 담겼어요.+잠깐 안부를 전해주면 어떨까요?",
        "연인이 당신을 걱정해요.+연인의 마음 한쪽이 조금 무거웠대요."
    )
    private val emojiNothing = listOf(
        "연인이 평범한 일상을 보냈대요.+안부를 전하는 일상이 관계를 지켜줄 거예요.",
        "감정의 큰 파도는 없지만,+OO님의 하루를 들어줄 누군가가 있다면 좋겠대요."
    )
    private val statusMessage = listOf(
        dummyList,
        emojiMiss,
        emojiTired,
        emojiSad,
        emojiGood,
        emojiAnnoy,
        emojiWorry,
        emojiNothing
    )

    @Async
    fun sendStatusAlarm(sender : Member, receiver : Member, emoji : Int) {
        val notificationContent =
            statusMessage.get(emoji).random()
        val parts = notificationContent.split("+")
        val title = parts[0]
        val content = if (parts[1].contains("OO")) {
            parts[1].replace("OO", sender.nickname)
        } else {
            parts[1]
        }
        val token = receiver.fcmToken
        fcmService.sendMessageTo(token, title, content)
    }
}