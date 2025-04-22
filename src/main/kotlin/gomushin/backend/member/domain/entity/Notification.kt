package gomushin.backend.member.domain.entity

import gomushin.backend.core.infrastructure.jpa.shared.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "notification")
class Notification(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(name = "member_id", nullable = false)
    val memberId: Long,

    @Column(name = "dday", nullable = false)
    var dday: Boolean = false,

    @Column(name = "partner_status", nullable = false)
    var partnerStatus: Boolean = false,

    ) : BaseEntity() {
    companion object {
        fun create(
            memberId: Long,
        ): Notification {
            return Notification(
                memberId = memberId
            )
        }
    }

    fun updateDday() {
        this.dday = !this.dday
    }

    fun updatePartnerStatus() {
        this.partnerStatus = !this.partnerStatus
    }
}
