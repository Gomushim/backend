package gomushin.backend.couple.facade

import gomushin.backend.core.CustomUserDetails
import gomushin.backend.couple.domain.service.CoupleConnectService
import gomushin.backend.couple.dto.request.CoupleAnniversaryRequest
import gomushin.backend.couple.dto.request.CoupleConnectRequest
import org.springframework.stereotype.Component

@Component
class CoupleFacade(
    private val coupleConnectService: CoupleConnectService
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
}
