package gomushin.backend.member.dto.request

import gomushin.backend.member.domain.value.Emotion
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated


data class UpdateMyEmotionAndStatusMessageRequest(
    @Enumerated(EnumType.STRING)
    @Schema(description = "이모지(MISS : 보고싶어요, GOOD: 기분 좋아요, COMMON : 아무느낌 없어요, " +
            "TIRED : 피곤해요, SAD: 서운해요, WORRY : 걱정돼요, ANGRY : 짜증나요)", example = "1")
    val emotion : Emotion,

    @Schema(description = "상태 메시지", example = "보고 싶어요")
    val statusMessage : String
)
