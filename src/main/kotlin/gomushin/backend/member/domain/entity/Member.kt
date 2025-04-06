package gomushin.backend.member.domain.entity

import gomushin.backend.core.infrastructure.jpa.shared.BaseEntity
import gomushin.backend.member.domain.value.Provider
import jakarta.persistence.*

@Entity
@Table(name = "member")
class Member(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    val name: String,

    @Column(unique = true)
    val email: String,

    val profileImageUrl: String,

    @Enumerated(EnumType.STRING)
    val provider: Provider,
): BaseEntity()  {
    companion object {
        fun create(
            name: String,
            email: String,
            profileImageUrl: String?,
            provider: Provider
        ): Member {
            return Member(
                name = name,
                email = email,
                profileImageUrl = profileImageUrl ?: "",
                provider = provider,
            )
        }
    }
}
