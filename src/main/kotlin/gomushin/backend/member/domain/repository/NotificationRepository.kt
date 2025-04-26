package gomushin.backend.member.domain.repository

import gomushin.backend.member.domain.entity.Notification
import org.springframework.data.jpa.repository.JpaRepository

interface NotificationRepository : JpaRepository<Notification, Long> {
    fun findByMemberId(memberId: Long): Notification?

    fun deleteAllByMemberId(memberId: Long)
}
