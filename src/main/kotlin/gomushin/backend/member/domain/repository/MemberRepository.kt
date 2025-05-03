package gomushin.backend.member.domain.repository

import gomushin.backend.member.domain.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface MemberRepository : JpaRepository<Member, Long> {
    fun findByEmail(email: String): Member?
    @Query(
        """
    SELECT m FROM Member m
    JOIN Notification n ON n.memberId = m.id
    WHERE m.isCouple = true
      AND NOT (n.dday = false AND n.partnerStatus = false)
    """
    )
    fun findCoupleMembersWithEnabledNotification(): List<Member>
}
