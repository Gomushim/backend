package gomushin.backend.member.domain.service

import gomushin.backend.core.infrastructure.exception.BadRequestException
import gomushin.backend.member.domain.entity.Member
import gomushin.backend.member.domain.repository.MemberRepository
import gomushin.backend.member.dto.request.UpdateMyEmotionAndStatusMessageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService(
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

    @Transactional
    fun updateMyEmotionAndStatusMessage(id: Long, updateMyEmotionAndStatusMessageRequest: UpdateMyEmotionAndStatusMessageRequest) {
        val member = getById(id)
        member.updateEmotion(updateMyEmotionAndStatusMessageRequest.emotion)
        member.updateStatusMessage(updateMyEmotionAndStatusMessageRequest.statusMessage)
    }
}
