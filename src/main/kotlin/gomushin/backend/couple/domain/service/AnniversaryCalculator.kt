package gomushin.backend.couple.domain.service

import gomushin.backend.core.infrastructure.exception.BadRequestException
import gomushin.backend.couple.domain.entity.Anniversary
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class AnniversaryCalculator {
    fun calculateInitAnniversaries(
        coupleId: Long,
        relationShipStartDate: LocalDate,
        militaryStartDate: LocalDate,
        militaryEndDate: LocalDate,
        anniversaryList: MutableList<Anniversary>,
    ): List<Anniversary> {

        if (militaryStartDate.isAfter(militaryEndDate)) throw BadRequestException("sarangggun.military.invalid-date")

        calculateAnniversariesBetweenMilitaryStartDateAndMilitaryEndDate(
            coupleId,
            relationShipStartDate,
            militaryStartDate,
            militaryEndDate,
            anniversaryList
        )
        calculateHundredsAnniversariesBetweenMilitaryStartDateAndMilitaryEndDate(
            coupleId,
            relationShipStartDate,
            militaryStartDate,
            militaryEndDate,
            anniversaryList
        )
        return anniversaryList
    }

    private fun calculateAnniversariesBetweenMilitaryStartDateAndMilitaryEndDate(
        coupleId: Long,
        relationShipStartDate: LocalDate,
        militaryStartDate: LocalDate,
        militaryEndDate: LocalDate,
        anniversaryList: MutableList<Anniversary>,
    ) {
        var anniversaryYear = 1L

        while (true) {
            val anniversaryDate = relationShipStartDate.plusYears(anniversaryYear)
            if (anniversaryDate.isAfter(militaryStartDate)) {
                break
            } else {
                anniversaryYear++
            }
        }

        while (true) {
            val anniversaryDate = relationShipStartDate.plusYears(anniversaryYear)
            if (anniversaryDate.isAfter(militaryEndDate)) {
                break
            } else {
                val title = "${anniversaryYear}주년"
                val anniversary = Anniversary.autoCreate(coupleId, title, anniversaryDate)
                anniversaryList.add(anniversary)
                anniversaryYear++
            }
        }
    }

    private fun calculateHundredsAnniversariesBetweenMilitaryStartDateAndMilitaryEndDate(
        coupleId: Long,
        relationShipStartDate: LocalDate,
        militaryStartDate: LocalDate,
        militaryEndDate: LocalDate,
        anniversaryList: MutableList<Anniversary>,
    ) {
        val plusDay = 100L
        var anniversaryDay = 0L
        var anniversaryDate = relationShipStartDate.minusDays(1)
        while (true) {
            anniversaryDate = anniversaryDate.plusDays(plusDay)
            anniversaryDay += plusDay
            if (anniversaryDate.isAfter(militaryEndDate)) {
                break
            } else if (anniversaryDate.isAfter(militaryStartDate) && anniversaryDate.isBefore(militaryEndDate)) {
                val title = "${anniversaryDay}일"
                val anniversary = Anniversary.autoCreate(coupleId, title, anniversaryDate)
                anniversaryList.add(anniversary)
            }
        }
    }
}
