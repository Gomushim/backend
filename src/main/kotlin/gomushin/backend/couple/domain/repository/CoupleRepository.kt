package gomushin.backend.couple.domain.repository

import gomushin.backend.couple.domain.entity.Couple
import jakarta.persistence.LockModeType
import jakarta.persistence.QueryHint
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.jpa.repository.QueryHints
import org.springframework.data.repository.query.Param

interface CoupleRepository : JpaRepository<Couple, Long> {
    fun findByInvitorId(invitorId: Long): Couple?
    fun findByInviteeId(inviteeId: Long): Couple?

    @Query("SELECT c FROM Couple c WHERE c.invitorId = :memberId OR c.inviteeId = :memberId")
    fun findByMemberId(@Param("memberId") memberId: Long): Couple?

    @Modifying
    @Query("DELETE FROM Couple c WHERE c.invitorId = :memberId OR c.inviteeId = :memberId")
    fun deleteByMemberId(@Param("memberId") memberId: Long)

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints(QueryHint(name = "javax.persistence.lock.timeout", value = "3000"))
    @Query("SELECT c FROM Couple c WHERE c.id = :id")
    fun findByIdWithLock(@Param("id") id: Long): Couple?
}
