package gomushin.backend.member.domain.entity

import gomushin.backend.core.infrastructure.jpa.shared.BaseEntity
import gomushin.backend.member.domain.value.Provider
import gomushin.backend.member.domain.value.Role
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "member")
class Member(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(name = "name", nullable = false)
    var name: String,

    @Column(name = "nickname", nullable = false)
    var nickname: String,

    @Column(name = "email", unique = true, nullable = false)
    var email: String,

    @Column(name = "birth_date")
    var birthDate: LocalDate? = null,

    @Column(name = "profile_image_url")
    var profileImageUrl: String?,

    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false)
    val provider: Provider,

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    var role: Role = Role.GUEST,

    @Column(name = "is_couple", nullable = false)
    var isCouple: Boolean = false,

    ) : BaseEntity() {
    companion object {
        fun create(
            name: String,
            nickname: String?,
            email: String,
            profileImageUrl: String?,
            provider: Provider
        ): Member {
            return Member(
                name = name,
                nickname = nickname ?: name,
                email = email,
                profileImageUrl = profileImageUrl ?: "",
                provider = provider,
            )
        }
    }

    fun updateCoupleStatus() {
        this.isCouple = !this.isCouple
    }
}
