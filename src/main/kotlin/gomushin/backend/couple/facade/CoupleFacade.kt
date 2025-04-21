package gomushin.backend.couple.facade

import gomushin.backend.core.CustomUserDetails
import gomushin.backend.couple.domain.service.CoupleConnectService
import gomushin.backend.couple.dto.request.CoupleAnniversaryRequest
import gomushin.backend.couple.domain.service.CoupleInfoService
import gomushin.backend.couple.dto.request.CoupleConnectRequest
import gomushin.backend.couple.dto.response.CoupleGradeResponse
import gomushin.backend.couple.dto.response.DdayResponse
import gomushin.backend.couple.dto.response.NicknameResponse
import gomushin.backend.couple.dto.response.StatusMessageResponse
import org.springframework.stereotype.Component

@Component
class CoupleFacade(
    private val coupleConnectService: CoupleConnectService,
    private val coupleInfoService: CoupleInfoService
) {

    fun requestCoupleCodeGeneration(customUserDetails: CustomUserDetails) =
        coupleConnectService.generateCoupleCode(customUserDetails.getId())

    fun requestCoupleConnect(
        customUserDetails: CustomUserDetails,
        request: CoupleConnectRequest
    ) = coupleConnectService.connectCouple(customUserDetails.getId(), request.coupleCode)

    fun registerAnniversary(
        customUserDetails: CustomUserDetails,
        request: CoupleAnniversaryRequest
    ) = coupleConnectService.registerAnniversary(
        customUserDetails.getId(),
        request
    )

    fun getGradeInfo(customUserDetails: CustomUserDetails): CoupleGradeResponse {
        val grade = coupleInfoService.getGrade(customUserDetails.getId())
        return CoupleGradeResponse.of(grade)
    }

    fun checkConnect(customUserDetails: CustomUserDetails): Boolean {
        return coupleInfoService.checkCouple(customUserDetails.getId())
    }

    fun getDday(customUserDetails: CustomUserDetails): DdayResponse {
        return coupleInfoService.getDday(customUserDetails.getId())
    }

    fun nickName(customUserDetails: CustomUserDetails) : NicknameResponse {
        return coupleInfoService.getNickName(customUserDetails.getId())
    }

    fun statusMessage(customUserDetails: CustomUserDetails): StatusMessageResponse {
        val statusMessage = coupleInfoService.getStatusMessage(customUserDetails.getId())
        return StatusMessageResponse.of(statusMessage)
    }
}
