package gomushin.backend.member.domain.repository

import gomushin.backend.member.domain.entity.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long> {
    fun findByName(name: String): Member?
    fun findByEmail(email: String): Member?
}
