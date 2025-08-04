package gomushin.backend.couple.domain.entity

import gomushin.backend.core.infrastructure.jpa.shared.BaseEntity
import gomushin.backend.couple.domain.value.Military
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "couple")
class Couple(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(name = "invitor_id", nullable = false)
    val invitorId: Long = 0L,

    @Column(name = "invitee_id", nullable = false)
    val inviteeId: Long = 0L,

    @Column(name = "relationship_start_date")
    var relationshipStartDate: LocalDate? = null,

    @Column(name = "military_start_date")
    var militaryStartDate: LocalDate? = null,

    @Column(name = "military_end_date")
    var militaryEndDate: LocalDate? = null,

    @Column(name = "army")
    @Enumerated(EnumType.STRING)
    var military: Military? = null,

    @Column(name = "is_anniversaries_registered")
    var isAnniversariesRegistered: Boolean = false,

    ) : BaseEntity() {
    companion object {
        fun of(
            invitorId: Long,
            inviteeId: Long,
        ): Couple {
            return Couple(
                invitorId = invitorId,
                inviteeId = inviteeId,
            )
        }
    }

    fun updateMilitary(
        military: String
    ) {
        this.military = Military.getByName(military)
    }

    fun updateAnniversary(
        relationshipStartDate: LocalDate?,
        militaryStartDate: LocalDate?,
        militaryEndDate: LocalDate?,
    ) {
        this.relationshipStartDate = relationshipStartDate ?: this.relationshipStartDate
        this.militaryStartDate = militaryStartDate ?: this.militaryStartDate
        this.militaryEndDate = militaryEndDate ?: this.militaryEndDate
    }

    fun containsUser(userId: Long): Boolean =
        userId == invitorId || userId == inviteeId

    fun initAnniversaries() {
        this.isAnniversariesRegistered = true
    }
}
