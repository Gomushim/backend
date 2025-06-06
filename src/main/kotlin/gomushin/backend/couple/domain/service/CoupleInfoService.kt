package gomushin.backend.couple.domain.service

import gomushin.backend.core.infrastructure.exception.BadRequestException
import gomushin.backend.couple.domain.entity.Anniversary
import gomushin.backend.couple.domain.entity.Couple
import gomushin.backend.couple.domain.repository.AnniversaryRepository
import gomushin.backend.couple.domain.repository.CoupleRepository
import gomushin.backend.couple.dto.request.UpdateMilitaryDateRequest
import gomushin.backend.couple.dto.request.UpdateRelationshipStartDateRequest
import gomushin.backend.couple.dto.response.DdayResponse
import gomushin.backend.couple.dto.response.NicknameResponse
import gomushin.backend.member.domain.entity.Member
import gomushin.backend.member.domain.repository.MemberRepository
import gomushin.backend.member.domain.value.Emotion
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.Period
import java.time.temporal.ChronoUnit

@Service
class CoupleInfoService(
        private val coupleRepository: CoupleRepository,
        private val memberRepository: MemberRepository,
        private val anniversaryRepository: AnniversaryRepository,
        private val anniversaryCalculator: AnniversaryCalculator
) {
    @Transactional(readOnly = true)
    fun getGrade(id: Long): Int {
        val couple = coupleRepository.findByMemberId(id)
                ?: throw BadRequestException("saranggun.couple.not-connected")
        val militaryStartDate = couple.militaryStartDate ?: throw BadRequestException("saranggun.couple.not-defined-militaryStartDate")
        val today = LocalDate.now()
        return computeGrade(militaryStartDate, today)
    }

    fun computeGrade(militaryStartDate: LocalDate, today: LocalDate) : Int {
        val period = Period.between(militaryStartDate, today)
        val totalMonths = period.years * 12 + period.months + if (period.days >= 1) 1 else 0

        return when {
            totalMonths < 2 -> 1
            totalMonths < 8 -> 2
            totalMonths < 14 -> 3
            else -> 4
        }
    }

    @Transactional(readOnly = true)
    fun checkCouple(id: Long): Boolean {
        return memberRepository.findById(id)
            .orElseThrow{ BadRequestException("sarangggun.member.not-exist-member") }
            .isCouple
    }

    @Transactional(readOnly = true)
    fun getDday(id: Long): DdayResponse {
        val couple = coupleRepository.findByMemberId(id)
                ?: throw BadRequestException("saranggun.couple.not-connected")
        val today = LocalDate.now()
        val sinceLove: Int? = couple.relationshipStartDate?.let { startLove ->
            computeDday(startLove, today) + 1
        }
        val sinceMilitaryStart : Int? = couple.militaryStartDate?.let { startMilitary ->
            computeDday(startMilitary, today)
        }
        val militaryEndLeft : Int? = couple.militaryEndDate?.let { endMilitary ->
            computeDday(endMilitary, today)
        }
        return DdayResponse.of(sinceLove, sinceMilitaryStart, militaryEndLeft)
    }

    fun computeDday(day1: LocalDate, day2: LocalDate) : Int {
        return ChronoUnit.DAYS.between(day1, day2).toInt()
    }

    @Transactional(readOnly = true)
    fun getNickName(id: Long): NicknameResponse {
        val userMember = memberRepository.findById(id).orElseThrow {
            BadRequestException("saranggun.member.not-found")
        }

        val coupleMember = findCoupleMember(id)

        return NicknameResponse.of(userMember.nickname, coupleMember.nickname)
    }

    @Transactional(readOnly = true)
    fun getStatusMessage(id: Long): String? {
        val coupleMember = findCoupleMember(id)
        return coupleMember.statusMessage
    }

    @Transactional(readOnly = true)
    fun findCoupleMember(id : Long): Member {
        val couple = coupleRepository.findByMemberId(id) ?: throw BadRequestException("saranggun.couple.not-connected")
        val coupleMemberId = if (couple.invitorId == id) couple.inviteeId else couple.invitorId

        val coupleMember = memberRepository.findById(coupleMemberId).orElseThrow {
            BadRequestException("sarangggun.couple.not-exist-couple")
        }
        return coupleMember
    }

    @Transactional
    fun updateMilitaryDate(couple: Couple, updateMilitaryDateRequest: UpdateMilitaryDateRequest) {
        updateAnniversary(couple, couple.relationshipStartDate!!, updateMilitaryDateRequest.militaryStartDate, updateMilitaryDateRequest.militaryEndDate)
        couple.updateAnniversary(couple.relationshipStartDate!!,
            updateMilitaryDateRequest.militaryStartDate,
            updateMilitaryDateRequest.militaryEndDate)
    }
    @Transactional
    fun updateRelationshipStartDate(couple: Couple, updateRelationshipStartDateRequest: UpdateRelationshipStartDateRequest) {
        updateAnniversary(couple, updateRelationshipStartDateRequest.relationshipStartDate, couple.militaryStartDate!!, couple.militaryEndDate!!)
        couple.updateAnniversary(updateRelationshipStartDateRequest.relationshipStartDate,
            couple.militaryStartDate,
            couple.militaryEndDate)
    }

    private fun updateAnniversary(couple: Couple,
                                  relationshipStartDate: LocalDate,
                                  militaryStartDate: LocalDate,
                                  militaryEndDate : LocalDate) {
        anniversaryRepository.deleteAnniversariesWithTitleEndingAndPropertyZero(couple.id)
        val anniversaries: MutableList<Anniversary> = mutableListOf()
        anniversaryCalculator.calculateInitAnniversaries(
            couple.id,
            relationshipStartDate,
            militaryStartDate,
            militaryEndDate,
            anniversaries
        )
        anniversaryRepository.saveAll(anniversaries)
    }

    @Transactional(readOnly = true)
    fun getCoupleEmotion(id: Long): Emotion {
        val coupleMember = findCoupleMember(id)
        return coupleMember.emotion ?: throw BadRequestException("sarangggun.member.not-exist-emoji")
    }
}