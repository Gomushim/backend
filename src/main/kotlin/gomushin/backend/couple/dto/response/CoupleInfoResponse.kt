package gomushin.backend.couple.dto.response

import gomushin.backend.couple.domain.entity.Couple
import io.swagger.v3.oas.annotations.media.Schema

data class CoupleInfoResponse(
    @Schema(description = "커플 ID", example = "1")
    val coupleId: Long,

    @Schema(description = "속해있는 군", example = "MARINE")
    val military: String,

    @Schema(description = "커플 기념일 초기화 되었는지 여부", example = "false")
    val isInit: Boolean = false,
) {
    companion object {
        fun of(couple: Couple): CoupleInfoResponse {
            return CoupleInfoResponse(
                coupleId = couple.id,
                military = couple.military.toString(),
                isInit = couple.isInit,
            )
        }
    }
}
