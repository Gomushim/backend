package gomushin.backend.member.domain.service

import gomushin.backend.core.infrastructure.exception.BadRequestException
import gomushin.backend.member.domain.entity.Member
import gomushin.backend.member.domain.repository.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberInfoService(
    private val memberRepository: MemberRepository,
) {

    @Transactional(readOnly = true)
    fun getGuestInfo(id: Long): Member =
        memberRepository.findById(id).orElseThrow { BadRequestException("sarangggun.member.not-exist-member") }

}
