package gomushin.backend.core.service

import gomushin.backend.core.CustomUserDetails
import gomushin.backend.core.infrastructure.exception.BadRequestException
import gomushin.backend.member.domain.repository.MemberRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(private val memberRepository: MemberRepository) : UserDetailsService {
    override fun loadUserByUsername(email: String): UserDetails {  // 파라미터명 username → email로 변경
        val member = memberRepository.findByEmail(email)
            ?: throw BadRequestException("sarangggun.member.not-exist-member")
        return CustomUserDetails(member)
    }

    fun loadUserById(id: Long): UserDetails =
        memberRepository.findById(id)
            .map { CustomUserDetails(it) }
            .orElseThrow { BadRequestException("sarangggun.member.not-exist-member") }
}
