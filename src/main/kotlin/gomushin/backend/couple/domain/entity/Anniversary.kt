package gomushin.backend.couple.domain.entity

import gomushin.backend.core.infrastructure.jpa.shared.BaseEntity
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "anniversary")
class Anniversary(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(name = "couple_id", nullable = false)
    val coupleId: Long = 0L,

    @Column(name = "title", nullable = false)
    var title: String,

    @Column(name = "anniversary_date", nullable = false)
    var anniversaryDate: LocalDate,

    @Column(name = "anniversary_property", nullable = false)
    var anniversaryProperty: Int
) : BaseEntity() {
    companion object {
        fun create(coupleId: Long, title: String, anniversaryDate: LocalDate): Anniversary {
            return Anniversary(
                coupleId = coupleId,
                title = title,
                anniversaryDate = anniversaryDate,
                anniversaryProperty = 0
            )
        }
    }
}
