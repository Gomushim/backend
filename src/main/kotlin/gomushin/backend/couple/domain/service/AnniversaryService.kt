package gomushin.backend.couple.domain.service

import gomushin.backend.couple.domain.entity.Anniversary
import gomushin.backend.couple.domain.entity.Couple
import gomushin.backend.couple.domain.repository.AnniversaryRepository
import gomushin.backend.couple.dto.request.GenerateAnniversaryRequest
import gomushin.backend.couple.dto.response.MonthlyAnniversariesResponse
import gomushin.backend.schedule.dto.response.DailyAnniversaryResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class AnniversaryService(
    private val anniversaryRepository: AnniversaryRepository,
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
    fun saveAll(anniversaries: List<Anniversary>): List<Anniversary> {
        return anniversaryRepository.saveAll(anniversaries)
    }

    @Transactional
    fun deleteAllByCoupleId(coupleId: Long) {
        return anniversaryRepository.deleteAllByCoupleId(coupleId)
    }

    @Transactional
    fun generateAnniversary(couple: Couple, generateAnniversaryRequest: GenerateAnniversaryRequest) {
        anniversaryRepository.save(
            Anniversary.manualCreate(
                couple.id,
                generateAnniversaryRequest.title,
                generateAnniversaryRequest.date,
                generateAnniversaryRequest.emoji
            )
        )
    }
}
