package gomushin.backend.couple.domain.service

import gomushin.backend.core.infrastructure.exception.BadRequestException
import gomushin.backend.couple.domain.entity.Couple
import gomushin.backend.couple.dto.request.CoupleAnniversaryRequest
import gomushin.backend.member.domain.service.MemberService
import gomushin.backend.member.util.CoupleCodeGeneratorUtil
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import java.time.LocalDate

@Service
class CoupleConnectService(
    private val redisTemplate: StringRedisTemplate,
    private val coupleService: CoupleService,
    private val memberService: MemberService,
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
    fun connectCouple(inviteeId: Long, coupleCode: String): Couple {
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

        val savedCouple = save(couple)
        delete(key)

        return savedCouple
    }

    @Transactional
    fun save(couple: Couple): Couple {
        updateCoupleStatus(couple.invitorId)
        updateCoupleStatus(couple.inviteeId)
        return coupleService.save(couple)
    }

    @Transactional
    fun updateCoupleStatus(userId: Long) {
        val member = memberService.getById(userId)
        member.updateCoupleStatus()
    }

    @Transactional
    fun registerAnniversary(
        userId: Long,
        request: CoupleAnniversaryRequest
    ) {
        val couple = coupleService.getById(request.coupleId)
        if (!checkUserInCouple(userId, couple)) {
            throw BadRequestException("sarangggun.couple.not-in-couple")
        }
        couple.updateAnniversary(
            relationshipStartDate = request.relationshipStartDate,
            militaryStartDate = request.militaryStartDate,
            militaryEndDate = request.militaryEndDate,
        )
    }

    private fun checkUserInCouple(userId: Long, couple: Couple): Boolean {
        val member = memberService.getById(userId)
        return member.id == couple.invitorId || member.id == couple.inviteeId
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
        val member = memberService.getById(userId)
        return member.isCouple
    }
}
