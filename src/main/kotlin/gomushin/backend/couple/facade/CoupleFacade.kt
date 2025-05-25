package gomushin.backend.couple.facade

import gomushin.backend.core.CustomUserDetails
import gomushin.backend.core.infrastructure.exception.BadRequestException
import gomushin.backend.couple.domain.entity.Anniversary
import gomushin.backend.couple.domain.entity.Couple
import gomushin.backend.couple.domain.service.*
import gomushin.backend.couple.dto.request.*
import gomushin.backend.couple.dto.response.*
import gomushin.backend.member.domain.service.MemberService
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class CoupleFacade(
    private val coupleConnectService: CoupleConnectService,
    private val anniversaryService: AnniversaryService,
    private val coupleInfoService: CoupleInfoService,
    private val coupleService: CoupleService,
    private val memberService: MemberService,
    private val anniversaryCalculator: AnniversaryCalculator
) {

    fun getInfo(customUserDetails: CustomUserDetails): CoupleInfoResponse {
        val member = memberService.getById(customUserDetails.getId())

        if (!member.checkIsCouple()) {
            throw BadRequestException("saranggun.couple.not-connected")
        }

        val couple = coupleService.getById(customUserDetails.getCouple().id)
        return CoupleInfoResponse.of(couple)
    }

    fun requestCoupleCodeGeneration(customUserDetails: CustomUserDetails) =
        coupleConnectService.generateCoupleCode(customUserDetails.getId())

    fun requestCoupleConnect(
        customUserDetails: CustomUserDetails,
        request: CoupleConnectRequest
    ) = coupleConnectService.connectCouple(customUserDetails.getId(), request.coupleCode)

    @Transactional
    fun registerAnniversary(
        customUserDetails: CustomUserDetails,
        request: CoupleAnniversaryRequest
    ) {
        val couple = coupleService.getByIdWithLock(request.coupleId)
        checkUserInCouple(customUserDetails.getId(), couple)
        checkCoupleAnniversaryIsInit(couple)

        couple.updateMilitary(request.military)

        couple.updateAnniversary(
            relationshipStartDate = request.relationshipStartDate,
            militaryStartDate = request.militaryStartDate,
            militaryEndDate = request.militaryEndDate,
        )

        val anniversaries: MutableList<Anniversary> = mutableListOf()

        anniversaryCalculator.calculateInitAnniversaries(
            couple.id,
            request.relationshipStartDate,
            request.militaryStartDate,
            request.militaryEndDate,
            anniversaries
        )

        couple.initAnniversaries()

        anniversaryService.saveAll(anniversaries)
    }

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

    fun nickName(customUserDetails: CustomUserDetails): NicknameResponse {
        return coupleInfoService.getNickName(customUserDetails.getId())
    }

    fun statusMessage(customUserDetails: CustomUserDetails): StatusMessageResponse {
        val statusMessage = coupleInfoService.getStatusMessage(customUserDetails.getId())
        return StatusMessageResponse.of(statusMessage)
    }

    fun updateMilitaryDate(customUserDetails: CustomUserDetails, updateMilitaryDateRequest: UpdateMilitaryDateRequest) {
        val couple = coupleService.getByMemberId(customUserDetails.getId())
        anniversaryService.deleteAllByCoupleIdAndAutoInsert(customUserDetails.getCouple())
        coupleInfoService.updateMilitaryDate(couple, updateMilitaryDateRequest)
    }

    fun updateRelationshipStartDate(
        customUserDetails: CustomUserDetails,
        updateRelationshipStartDateRequest: UpdateRelationshipStartDateRequest
    ) {
        val couple = coupleService.getByMemberId(customUserDetails.getId())
        coupleInfoService.updateRelationshipStartDate(couple, updateRelationshipStartDateRequest)
    }

    fun getCoupleEmotion(customUserDetails: CustomUserDetails): CoupleEmotionResponse {
        val emotion = coupleInfoService.getCoupleEmotion(customUserDetails.getId())
        return CoupleEmotionResponse.of(emotion)
    }

    fun generateAnniversary(
        customUserDetails: CustomUserDetails,
        generateAnniversaryRequest: GenerateAnniversaryRequest
    ) {
        anniversaryService.generateAnniversary(customUserDetails.getCouple(), generateAnniversaryRequest)
    }

    private fun checkUserInCouple(userId: Long, couple: Couple) {
        if (!couple.containsUser(userId)) {
            throw BadRequestException("sarangggun.couple.not-in-couple")
        }
    }

    private fun checkCoupleAnniversaryIsInit(couple: Couple) {
        if (couple.isAnniversariesRegistered) {
            throw BadRequestException("sarangggun.couple.already-init")
        }
    }

    fun getCoupleBirthDay(customUserDetails: CustomUserDetails): CoupleBirthDayResponse {
        val couple = coupleInfoService.findCoupleMember(customUserDetails.getId())
        val member = memberService.getById(customUserDetails.getId())
        return CoupleBirthDayResponse.of(couple, member)
    }
}
