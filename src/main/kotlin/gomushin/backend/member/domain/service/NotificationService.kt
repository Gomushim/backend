package gomushin.backend.member.domain.service

import gomushin.backend.core.infrastructure.exception.BadRequestException
import gomushin.backend.member.domain.entity.Notification
import gomushin.backend.member.domain.repository.NotificationRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class NotificationService(
    private val notificationRepository: NotificationRepository
) {

    @Transactional
    fun initNotification(memberId: Long, isNotification: Boolean) {
        val notification = Notification.create(memberId)
        if (isNotification) {
            notification.updateDday(true)
            notification.updatePartnerStatus(true)
        }
        save(notification)
    }

    @Transactional(readOnly = true)
    fun getByMemberId(memberId: Long): Notification {
        return findByMemberId(memberId) ?: throw BadRequestException("sarangggun.member.not-exist-member")
    }

    @Transactional(readOnly = true)
    fun findByMemberId(memberId: Long): Notification? {
        return notificationRepository.findByMemberId(memberId)
    }

    @Transactional
    fun save(notification: Notification): Notification {
        return notificationRepository.save(notification)
    }

    @Transactional
    fun deleteAllByMember(memberId: Long) {
        notificationRepository.deleteAllByMemberId(memberId)
    }
}
