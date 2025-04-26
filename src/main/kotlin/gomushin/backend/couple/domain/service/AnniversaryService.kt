package gomushin.backend.couple.domain.service

import gomushin.backend.core.infrastructure.exception.BadRequestException
import gomushin.backend.couple.domain.entity.Anniversary
import gomushin.backend.couple.domain.entity.Couple
import gomushin.backend.couple.domain.repository.AnniversaryRepository
import gomushin.backend.couple.dto.request.CoupleAnniversaryRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AnniversaryService(
    private val anniversaryRepository: AnniversaryRepository,
    private val coupleService: CoupleService,
    private val anniversaryCalculator: AnniversaryCalculator
) {

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

    private fun checkUserInCouple(userId: Long, couple: Couple) {
        if (!couple.containsUser(userId)) {
            throw BadRequestException("sarangggun.couple.not-in-couple")
        }
    }
}
