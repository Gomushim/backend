package gomushin.backend.member.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size


data class UpdateMyEmotionAndStatusMessageRequest(
    @Schema(description = "이모지(1 : 보고싶어요, 2: 기분 좋아요, 3 : 아무느낌 없어요, " +
            "4 : 피곤해요, 5: 서운해요, 6 : 걱정돼요, 7 : 짜증나요)", example = "1")
    @field:NotNull(message = "sarangggun.member.invalid-emotion")
    @field:Min(1, message = "sarangggun.member.invalid-emotion")
    @field:Max(7, message = "sarangggun.member.invalid-emotion")
    val emotion : Int,

    @Schema(description = "상태 메시지", example = "보고 싶어요")
    @field:NotNull(message = "sarangggun.member.empty-status-message")
    @field:Size(max = 25, message = "sarangggun.member.status-message-too-long")
    val statusMessage : String
)
