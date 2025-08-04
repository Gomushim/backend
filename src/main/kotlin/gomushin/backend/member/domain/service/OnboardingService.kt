package gomushin.backend.member.domain.service

import gomushin.backend.core.infrastructure.exception.BadRequestException
import gomushin.backend.member.domain.entity.Member
import gomushin.backend.member.domain.repository.MemberRepository
import gomushin.backend.member.domain.value.Role
import gomushin.backend.member.dto.request.OnboardingRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OnboardingService(
    private val memberRepository: MemberRepository,
) {
    @Transactional
    fun onboarding(id: Long, onboardingRequest: OnboardingRequest) {
        val member = getById(id)
        member.nickname = onboardingRequest.nickname
        member.birthDate = onboardingRequest.birthDate
        member.role = Role.MEMBER
        member.fcmToken = onboardingRequest.fcmToken
    }

    @Transactional(readOnly = true)
    fun getById(id: Long): Member {
        return findById(id) ?: throw BadRequestException("sarangggun.member.not-exist-member")
    }

    @Transactional(readOnly = true)
    fun findById(id: Long): Member? {
        return memberRepository.findByIdOrNull(id)
    }
}
