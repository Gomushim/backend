package gomushin.backend.core

import gomushin.backend.core.infrastructure.exception.BadRequestException
import gomushin.backend.member.domain.entity.Member
import gomushin.backend.member.domain.repository.MemberRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(private val memberRepository: MemberRepository) : UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        if (username == null) {
            throw BadRequestException("saranggun.member.not-provided-name")
        }

        return memberRepository.findByName(username!!)?.let { createUserDetail(it) }
            ?: throw BadRequestException("sarangggun.member.not-exist-member")
    }

    fun loadUserById(id: Long): UserDetails =
        memberRepository.findById(id).map { createUserDetail(it) }
            .orElseThrow { BadRequestException("sarangggun.member.not-exist-member") }

    private fun createUserDetail(member: Member): UserDetails {
        return CustomUserDetails(member)
    }
}
