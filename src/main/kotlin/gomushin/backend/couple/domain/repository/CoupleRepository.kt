package gomushin.backend.couple.domain.repository

import gomushin.backend.couple.domain.entity.Couple
import org.springframework.data.jpa.repository.JpaRepository

interface CoupleRepository : JpaRepository<Couple, Long> {
    fun findByInvitorId(invitorId: Long) : Couple?
    fun findByInviteeId(inviteeId: Long) : Couple?
}
