package gomushin.backend.couple.domain.service

import gomushin.backend.core.infrastructure.exception.BadRequestException
import gomushin.backend.couple.domain.entity.Couple
import gomushin.backend.couple.domain.repository.CoupleRepository
import gomushin.backend.member.domain.repository.MemberRepository
import gomushin.backend.member.util.CoupleCodeGeneratorUtil
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration

@Service
class CoupleConnectService(
    private val redisTemplate: StringRedisTemplate,
    private val coupleRepository: CoupleRepository,
    private val memberRepository: MemberRepository,
) {

    companion object {
        private val COUPLE_CODE_DURATION = Duration.ofMinutes(60)
        private const val COUPLE_CODE_PREFIX = "COUPLE_CODE:"
    }

    fun generateCoupleCode(userId: Long): String {
        val coupleCode = CoupleCodeGeneratorUtil.generateCoupleCode()
        val key = getCoupleCodeKey(coupleCode)
        redisTemplate.opsForValue().set(key, userId.toString(), COUPLE_CODE_DURATION)
        return coupleCode
    }

    @Transactional
    fun connectCouple(inviteeId: Long, coupleCode: String) {
        val key = getCoupleCodeKey(coupleCode)
        val invitorId = getCoupleCodeOrNull(key)
            ?: throw BadRequestException("sarangggun.couple.invalid-couple-code")
        if (invitorId == inviteeId) {
            throw BadRequestException("sarangggun.couple.couple-code-same")
        }

        val couple = Couple.of(
            invitorId,
            inviteeId,
        )

        if (isAlreadyConnected(inviteeId) || isAlreadyConnected(invitorId)) {
            throw BadRequestException("sarangggun.couple.already-connected")
        }

        save(couple)
        delete(key)
    }

    @Transactional
    fun save(couple: Couple): Couple {
        updateCoupleStatus(couple.invitorId)
        updateCoupleStatus(couple.inviteeId)
        return coupleRepository.save(couple)
    }

    @Transactional
    fun updateCoupleStatus(userId: Long) {
        val member = memberRepository.findById(userId).orElseThrow {
            BadRequestException("sarangggun.member.not-found")
        }
        member.updateCoupleStatus()
    }

    private fun getCoupleCodeOrNull(key: String): Long? {
        return redisTemplate.opsForValue().get(key)?.toLongOrNull()
    }

    private fun delete(key: String) {
        redisTemplate.delete(key)
    }

    private fun getCoupleCodeKey(code: String): String {
        return "$COUPLE_CODE_PREFIX$code"
    }

    private fun isAlreadyConnected(userId: Long): Boolean {
        val member = memberRepository.findById(userId).orElseThrow {
            BadRequestException("sarangggun.member.not-found")
        }

        return member.isCouple
    }
}
