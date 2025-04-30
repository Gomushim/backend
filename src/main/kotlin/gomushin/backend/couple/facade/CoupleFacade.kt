package gomushin.backend.couple.facade

import gomushin.backend.core.CustomUserDetails
import gomushin.backend.couple.domain.service.AnniversaryService
import gomushin.backend.couple.domain.service.CoupleConnectService
import gomushin.backend.couple.domain.service.CoupleInfoService
import gomushin.backend.couple.domain.service.CoupleService
import gomushin.backend.couple.dto.request.*
import gomushin.backend.couple.dto.response.*
import org.springframework.stereotype.Component

@Component
class CoupleFacade(
    private val coupleConnectService: CoupleConnectService,
    private val anniversaryService: AnniversaryService,
    private val coupleInfoService: CoupleInfoService,
    private val coupleService: CoupleService
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
    ) = anniversaryService.registerAnniversary(
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

    fun updateMilitaryDate(customUserDetails: CustomUserDetails, updateMilitaryDateRequest: UpdateMilitaryDateRequest) {
        val couple = coupleService.getByMemberId(customUserDetails.getId())
        coupleInfoService.updateMilitaryDate(couple, updateMilitaryDateRequest)
    }

    fun updateRelationshipStartDate(customUserDetails: CustomUserDetails, updateRelationshipStartDateRequest: UpdateRelationshipStartDateRequest) {
        val couple = coupleService.getByMemberId(customUserDetails.getId())
        coupleInfoService.updateRelationshipStartDate(couple, updateRelationshipStartDateRequest)
    }

    fun getCoupleEmotion(customUserDetails: CustomUserDetails): CoupleEmotionResponse {
        val emotion = coupleInfoService.getCoupleEmotion(customUserDetails.getId())
        return CoupleEmotionResponse.of(emotion)
    }

    fun generateAnniversary(customUserDetails: CustomUserDetails, generateAnniversaryRequest: GenerateAnniversaryRequest) {
        anniversaryService.generateAnniversary(customUserDetails.getCouple(), generateAnniversaryRequest)
    }
}
