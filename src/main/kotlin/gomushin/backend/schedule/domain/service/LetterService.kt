package gomushin.backend.schedule.domain.service

import gomushin.backend.core.infrastructure.exception.BadRequestException
import gomushin.backend.schedule.domain.entity.Letter
import gomushin.backend.schedule.domain.repository.LetterRepository
import gomushin.backend.schedule.dto.UpsertLetterRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class LetterService(
    private val letterRepository: LetterRepository,
) {
    @Transactional
    fun upsert(authorId: Long, upsertLetterRequest: UpsertLetterRequest): Letter {
        return upsertLetterRequest.letterId?.let { letterId ->
            getById(letterId).apply {
                title = upsertLetterRequest.title
                content = upsertLetterRequest.content
            }.let { savedLetter ->
                save(savedLetter)
            }
        } ?: save(
            Letter.of(
                scheduleId = upsertLetterRequest.scheduleId,
                authorId = authorId,
                title = upsertLetterRequest.title,
                content = upsertLetterRequest.content,
            )
        )

    }

    @Transactional(readOnly = true)
    fun getById(id: Long) = findById(id) ?: throw BadRequestException("sarangggun.letter.not-exist")

    @Transactional(readOnly = true)
    fun findById(id: Long) = letterRepository.findByIdOrNull(id)

    @Transactional
    fun save(letter: Letter) = letterRepository.save(letter)

    @Transactional
    fun delete(letterId: Long) {
        letterRepository.deleteById(letterId)
    }
    @Transactional
    fun deleteAllByMemberId(memberId : Long) {
        letterRepository.deleteAllByAuthorId(memberId)
    }

    @Transactional(readOnly = true)
    fun findAllByAuthorId(memberId: Long) : List<Long>{
        return letterRepository.findAllByAuthorId(memberId)
    }
}
