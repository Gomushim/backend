package gomushin.backend.couple.domain.service

import gomushin.backend.core.infrastructure.exception.BadRequestException
import gomushin.backend.couple.domain.entity.Anniversary
import gomushin.backend.couple.domain.entity.Couple
import gomushin.backend.couple.domain.repository.AnniversaryRepository
import gomushin.backend.couple.dto.request.GenerateAnniversaryRequest
import gomushin.backend.couple.dto.response.AnniversaryNotificationInfo
import gomushin.backend.couple.dto.response.MonthlyAnniversariesResponse
import gomushin.backend.schedule.dto.response.DailyAnniversaryResponse
import gomushin.backend.schedule.dto.response.MainAnniversariesResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class AnniversaryService(
    private val anniversaryRepository: AnniversaryRepository,
) {

    @Transactional(readOnly = true)
    fun getById(id: Long): Anniversary {
        return findById(id) ?: throw BadRequestException("sarangggun.anniversary.not-found")
    }

    @Transactional(readOnly = true)
    fun findById(id: Long) = anniversaryRepository.findByIdOrNull(id)

    @Transactional(readOnly = true)
    fun findAnniversaries(couple: Couple, pageRequest: PageRequest): Page<Anniversary> {
        return anniversaryRepository.findAnniversaries(
            couple.id,
            pageRequest
        )
    }

    @Transactional(readOnly = true)
    fun findByCoupleAndDateBetween(
        couple: Couple,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<MainAnniversariesResponse> {
        return anniversaryRepository.findByCoupleIdAndDateBetween(couple.id, startDate, endDate)
    }

    @Transactional(readOnly = true)
    fun findByCoupleAndYearAndMonth(couple: Couple, year: Int, month: Int): List<MonthlyAnniversariesResponse> {
        return anniversaryRepository.findByCoupleIdAndYearAndMonth(couple.id, year, month)
    }

    @Transactional(readOnly = true)
    fun findByCoupleAndDate(couple: Couple, date: LocalDate): List<DailyAnniversaryResponse> {
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

    @Transactional(readOnly = true)
    fun getUpcomingTop3Anniversaries(couple: Couple): List<Anniversary> {
        return anniversaryRepository.findTop3UpcomingAnniversaries(couple.id)
    }

    @Transactional(readOnly = true)
    fun getTodayAnniversaryMemberFcmTokens(date: LocalDate): List<AnniversaryNotificationInfo> {
        return anniversaryRepository.findTodayAnniversaryMemberFcmTokens(date)
    }

    @Transactional
    fun delete(couple: Couple, anniversaryId: Long) {
        val anniversary = getById(anniversaryId)
        if (anniversary.coupleId != couple.id) {
            throw BadRequestException("sarangggun.anniversary.unauthorized")
        }
        anniversaryRepository.deleteById(anniversaryId)
    }
}
