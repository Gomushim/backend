package gomushin.backend.couple.domain.service

import gomushin.backend.core.infrastructure.exception.BadRequestException
import gomushin.backend.couple.domain.entity.Couple
import gomushin.backend.couple.domain.repository.CoupleRepository
import gomushin.backend.couple.dto.response.DdayResponse
import gomushin.backend.couple.dto.response.NicknameResponse
import gomushin.backend.member.domain.repository.MemberRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.Period
import java.time.temporal.ChronoUnit

@Service
class CoupleInfoService(
        private val coupleRepository: CoupleRepository,
        private val memberRepository: MemberRepository
) {
    fun getGrade(id: Long): Int {
        val couple = getCouple(id)
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

    fun getCouple(id : Long): Couple? {
        return coupleRepository.findByInvitorId(id)
                ?: coupleRepository.findByInviteeId(id)
    }

    fun checkCouple(id: Long): Boolean = getCouple(id) != null
    fun getDday(id: Long): DdayResponse {
        val couple = getCouple(id)
                ?: throw BadRequestException("saranggun.couple.not-connected")
        val today = LocalDate.now()
        val sinceLove: Int? = couple.relationshipStartDate?.let { startLove ->
            computeDday(startLove, today)
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

    fun nickName(id: Long): NicknameResponse {
        val userMember = memberRepository.findById(id).orElseThrow {
            BadRequestException("saranggun.member.not-found")
        }

        val couple = getCouple(id) ?: throw BadRequestException("saranggun.couple.not-connected")
        val coupleMemberId = if (couple.invitorId == id) couple.inviteeId else couple.invitorId

        val coupleMember = memberRepository.findById(coupleMemberId).orElseThrow {
            BadRequestException("sarangggun.couple.not-exist-couple")
        }

        return NicknameResponse.of(userMember.nickname, coupleMember.nickname)
    }

}