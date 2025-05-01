package gomushin.backend.member.domain.repository

import gomushin.backend.member.domain.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface MemberRepository : JpaRepository<Member, Long> {
    fun findByEmail(email: String): Member?
    @Query("SELECT m FROM Member m WHERE m.isCouple = true")
    fun findCoupledMembers(): List<Member>
}
