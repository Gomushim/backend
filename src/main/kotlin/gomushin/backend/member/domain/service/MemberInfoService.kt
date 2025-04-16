package gomushin.backend.member.domain.service

import gomushin.backend.core.infrastructure.exception.BadRequestException
import gomushin.backend.member.domain.entity.Member
import gomushin.backend.member.domain.repository.MemberRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberInfoService(
    private val memberRepository: MemberRepository,
) {

    @Transactional(readOnly = true)
    fun getById(id: Long): Member {
        return findById(id) ?: throw BadRequestException("sarangggun.member.not-exist-member")
    }

    @Transactional(readOnly = true)
    fun findById(id: Long): Member? {
        return memberRepository.findByIdOrNull(id)
    }
}
