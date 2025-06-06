package gomushin.backend.couple.domain.service

import gomushin.backend.core.infrastructure.exception.BadRequestException
import gomushin.backend.couple.domain.entity.Couple
import gomushin.backend.couple.domain.repository.CoupleRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CoupleService(
    private val coupleRepository: CoupleRepository
) {
    @Transactional(readOnly = true)
    fun getById(id: Long): Couple {
        return findById(id) ?: throw BadRequestException("sarangggun.couple.not-found")
    }

    @Transactional(readOnly = true)
    fun getByIdWithLock(id: Long): Couple {
        return findByIdWithLock(id) ?: throw BadRequestException("sarangggun.couple.not-found")
    }

    @Transactional(readOnly = true)
    fun findByIdWithLock(id: Long): Couple? {
        return coupleRepository.findByIdWithLock(id)
    }

    @Transactional(readOnly = true)
    fun findById(id: Long): Couple? {
        return coupleRepository.findByIdOrNull(id)
    }

    @Transactional
    fun save(couple: Couple): Couple {
        return coupleRepository.save(couple)
    }

    @Transactional(readOnly = true)
    fun getByMemberId(memberId: Long): Couple {
        return findByMemberId(memberId) ?: throw BadRequestException("sarangggun.couple.not-found")
    }

    @Transactional(readOnly = true)
    fun findByMemberId(memberId: Long): Couple? {
        return coupleRepository.findByMemberId(memberId)
    }

    @Transactional
    fun deleteByMemberId(memberId: Long) {
        coupleRepository.deleteByMemberId(memberId)
    }
}
