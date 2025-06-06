package gomushin.backend.schedule.domain.service

import gomushin.backend.core.infrastructure.exception.BadRequestException
import gomushin.backend.couple.domain.entity.Couple
import gomushin.backend.schedule.domain.entity.Letter
import gomushin.backend.schedule.domain.entity.Schedule
import gomushin.backend.schedule.domain.repository.LetterRepository
import gomushin.backend.schedule.dto.request.UpsertLetterRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class LetterService(
    private val letterRepository: LetterRepository,
) {
    @Transactional
    fun upsert(authorId: Long, authorName: String, couple: Couple, upsertLetterRequest: UpsertLetterRequest): Letter {
        return upsertLetterRequest.letterId?.let { letterId ->
            getById(letterId).apply {
                title = upsertLetterRequest.title
                content = upsertLetterRequest.content
            }.let { savedLetter ->
                save(savedLetter)
            }
        } ?: save(
            Letter.of(
                coupleId = couple.id,
                scheduleId = upsertLetterRequest.scheduleId,
                authorId = authorId,
                author = authorName,
                title = upsertLetterRequest.title,
                content = upsertLetterRequest.content,
            )
        )
    }

    @Transactional(readOnly = true)
    fun findByCoupleIdAndScheduleId(couple: Couple, scheduleId: Long): List<Letter> {
        return letterRepository.findByCoupleIdAndScheduleId(couple.id, scheduleId)
    }

    @Transactional(readOnly = true)
    fun getById(id: Long) = findById(id) ?: throw BadRequestException("sarangggun.letter.not-exist")

    @Transactional(readOnly = true)
    fun findById(id: Long) = letterRepository.findByIdOrNull(id)

    @Transactional(readOnly = true)
    fun findByCouple(couple: Couple) = letterRepository.findByCoupleId(couple.id)

    @Transactional(readOnly = true)
    fun getByCoupleAndScheduleAndId(
        couple: Couple,
        schedule: Schedule,
        letterId: Long,
    ) = findByCoupleIdAndScheduleIdAndId(couple, schedule.id, letterId)
        ?: throw BadRequestException("sarangggun.letter.not-exist")

    @Transactional(readOnly = true)
    fun findByCoupleIdAndScheduleIdAndId(couple: Couple, scheduleId: Long, letterId: Long) =
        letterRepository.findByCoupleIdAndScheduleIdAndId(couple.id, scheduleId, letterId)

    @Transactional(readOnly = true)
    fun findByCoupleAndSchedule(couple: Couple, schedule: Schedule) =
        letterRepository.findByCoupleIdAndScheduleId(couple.id, schedule.id)

    @Transactional
    fun save(letter: Letter) = letterRepository.save(letter)

    @Transactional
    fun delete(letterId: Long) {
        letterRepository.deleteById(letterId)
    }

    @Transactional
    fun deleteAllByMemberId(memberId: Long) {
        letterRepository.deleteAllByAuthorId(memberId)
    }

    @Transactional(readOnly = true)
    fun findAllByAuthorId(memberId: Long): List<Long> {
        return letterRepository.findAllByAuthorId(memberId)
    }

    @Transactional(readOnly = true)
    fun findAllToCouple(
        couple: Couple,
        pageable: Pageable
    ): Page<Letter> {
        return letterRepository.findAllToCouple(
            couple.id,
            pageable
        )
    }

    @Transactional(readOnly = true)
    fun findTop5ByCreatedDateDesc(couple: Couple): List<Letter> {
        return letterRepository.findTop5ByCoupleIdOrderByCreatedAtDesc(couple.id)
    }
}
