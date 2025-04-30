package gomushin.backend.couple.domain.service

import gomushin.backend.core.infrastructure.exception.BadRequestException
import gomushin.backend.couple.domain.entity.Anniversary
import gomushin.backend.couple.domain.entity.Couple
import gomushin.backend.couple.domain.repository.AnniversaryRepository
import gomushin.backend.couple.dto.request.CoupleAnniversaryRequest
import gomushin.backend.couple.dto.request.GenerateAnniversaryRequest
import gomushin.backend.couple.dto.response.MonthlyAnniversariesResponse
import gomushin.backend.schedule.dto.response.DailyAnniversaryResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class AnniversaryService(
    private val anniversaryRepository: AnniversaryRepository,
    private val coupleService: CoupleService,
    private val anniversaryCalculator: AnniversaryCalculator
) {

    @Transactional(readOnly = true)
    fun findByCoupleIdAndYearAndMonth(couple: Couple, year: Int, month: Int): List<MonthlyAnniversariesResponse> {
        return anniversaryRepository.findByCoupleIdAndYearAndMonth(couple.id, year, month)
    }

    @Transactional(readOnly = true)
    fun findByDate(couple: Couple, date: LocalDate): List<DailyAnniversaryResponse> {
        return anniversaryRepository.findByCoupleIdAndDate(couple.id, date)
    }

    @Transactional
    fun registerAnniversary(
        userId: Long,
        request: CoupleAnniversaryRequest
    ) {
        val couple = coupleService.getById(request.coupleId)
        checkUserInCouple(userId, couple)

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

        saveAll(anniversaries)
    }

    @Transactional
    fun saveAll(anniversaries: List<Anniversary>): List<Anniversary> {
        return anniversaryRepository.saveAll(anniversaries)
    }

    @Transactional
    fun deleteAllByCoupleId(coupleId : Long) {
        return anniversaryRepository.deleteAllByCoupleId(coupleId)
    }

    @Transactional
    fun generateAnniversary(couple: Couple, generateAnniversaryRequest: GenerateAnniversaryRequest) {
        anniversaryRepository.save(Anniversary.manualCreate(
            couple.id,
            generateAnniversaryRequest.title,
            generateAnniversaryRequest.date,
            generateAnniversaryRequest.emoji
        ))
    }

    private fun checkUserInCouple(userId: Long, couple: Couple) {
        if (!couple.containsUser(userId)) {
            throw BadRequestException("sarangggun.couple.not-in-couple")
        }
    }
}
