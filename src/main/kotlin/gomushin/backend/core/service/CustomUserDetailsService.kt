package gomushin.backend.core.service

import gomushin.backend.core.CustomUserDetails
import gomushin.backend.core.infrastructure.exception.BadRequestException
import gomushin.backend.couple.domain.repository.CoupleRepository
import gomushin.backend.member.domain.repository.MemberRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val memberRepository: MemberRepository,
    private val coupleRepository: CoupleRepository,
) : UserDetailsService {
    override fun loadUserByUsername(email: String): UserDetails {
        val member = memberRepository.findByEmail(email)
            ?: throw BadRequestException("sarangggun.member.not-exist-member")
        return CustomUserDetails(member, coupleRepository)
    }

    fun loadUserById(id: Long): UserDetails =
        memberRepository.findById(id)
            .map { CustomUserDetails(it, coupleRepository) }
            .orElseThrow { BadRequestException("sarangggun.member.not-exist-member") }
}
