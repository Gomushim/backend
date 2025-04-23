package gomushin.backend.member.dto.request

import io.swagger.v3.oas.annotations.media.Schema


data class UpdateMyEmotionAndStatusMessageRequest(
    @Schema(description = "이모지(1 : 보고싶어요, 2: 기분 좋아요, 3 : 아무느낌 없어요, " +
            "4 : 피곤해요, 5: 서운해요, 6 : 걱정돼요, 7 : 짜증나요)", example = "1")
    val emotion : Int,

    @Schema(description = "상태 메시지", example = "보고 싶어요")
    val statusMessage : String
)
