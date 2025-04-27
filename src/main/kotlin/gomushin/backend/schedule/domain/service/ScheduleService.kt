package gomushin.backend.schedule.domain.service

import gomushin.backend.core.infrastructure.exception.BadRequestException
import gomushin.backend.couple.domain.entity.Couple
import gomushin.backend.schedule.domain.entity.Schedule
import gomushin.backend.schedule.domain.repository.ScheduleRepository
import gomushin.backend.schedule.dto.request.UpsertScheduleRequest
import gomushin.backend.schedule.dto.response.DailyScheduleResponse
import gomushin.backend.schedule.dto.response.MonthlySchedulesResponse
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class ScheduleService(
    private val scheduleRepository: ScheduleRepository,
) {
    @Transactional(readOnly = true)
    fun findByCoupleIdAndYearAndMonth(couple: Couple, year: Int, month: Int): List<MonthlySchedulesResponse> {
        return scheduleRepository.findByCoupleIdAndYearAndMonth(couple.id, year, month)
    }

    @Transactional(readOnly = true)
    fun findByDate(couple: Couple, date: LocalDate): List<DailyScheduleResponse> {
        return scheduleRepository.findByCoupleIdAndStartDate(couple.id, date)
    }

    @Transactional
    fun upsert(id: Long?, coupleId: Long, userId: Long, upsertScheduleRequest: UpsertScheduleRequest) {
        id?.let {
            getById(id).let {
                it.startDate = upsertScheduleRequest.startDate
                it.endDate = upsertScheduleRequest.endDate
                it.title = upsertScheduleRequest.title
                it.fatigue = upsertScheduleRequest.fatigue
                it.isAllDay = upsertScheduleRequest.isAllDay
            }
        } ?: save(upsertScheduleRequest.toEntity(coupleId, userId))
    }

    @Transactional(readOnly = true)
    fun getById(id: Long) = findById(id) ?: throw BadRequestException("sarangggun.schedule.not-exist-schedule")

    @Transactional(readOnly = true)
    fun findById(id: Long) = scheduleRepository.findByIdOrNull(id)

    @Transactional
    fun save(schedule: Schedule) = scheduleRepository.save(schedule)

    @Transactional
    fun delete(coupleId: Long, userId: Long, scheduleId: Long) {
        val schedule = getById(scheduleId)
        if (schedule.coupleId != coupleId || schedule.userId != userId) {
            throw BadRequestException("sarangggun.schedule.unauthorized")
        }
        scheduleRepository.deleteById(scheduleId)
    }

    @Transactional
    fun deleteAllByMember(memberId : Long) {
        scheduleRepository.deleteAllByUserId(memberId)
    }
}
