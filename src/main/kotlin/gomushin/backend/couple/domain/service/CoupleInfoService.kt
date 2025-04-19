package gomushin.backend.couple.domain.service

import gomushin.backend.core.infrastructure.exception.BadRequestException
import gomushin.backend.couple.domain.repository.CoupleRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.Period

@Service
class CoupleInfoService(
        private val coupleRepository: CoupleRepository
) {
    fun getGrade(id: Long): Int {
        val couple = coupleRepository.findByInvitorId(id)
                ?: coupleRepository.findByInviteeId(id)
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

}