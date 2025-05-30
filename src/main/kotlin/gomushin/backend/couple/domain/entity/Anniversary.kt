package gomushin.backend.couple.domain.entity

import gomushin.backend.core.infrastructure.jpa.shared.BaseEntity
import gomushin.backend.couple.domain.value.AnniversaryEmoji
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
    var anniversaryProperty: Int,

    @Enumerated(EnumType.STRING)
    @Column(name = "anniversary_emoji")
    var emoji: AnniversaryEmoji? = null,

    @Column(name = "is_auto_insert")
    var isAutoInsert: Boolean = false,
) : BaseEntity() {
    companion object {
        fun autoCreate(coupleId: Long, title: String, anniversaryDate: LocalDate): Anniversary {
            return Anniversary(
                coupleId = coupleId,
                title = title,
                anniversaryDate = anniversaryDate,
                anniversaryProperty = 0,
                emoji = AnniversaryEmoji.HEART,
                isAutoInsert = true,
            )
        }

        fun manualCreate(
            coupleId: Long,
            title: String,
            anniversaryDate: LocalDate,
            emoji: AnniversaryEmoji
        ): Anniversary {
            return Anniversary(
                coupleId = coupleId,
                title = title,
                anniversaryDate = anniversaryDate,
                anniversaryProperty = 1,
                emoji = emoji
            )
        }
    }

    fun update(title: String?, anniversaryDate: LocalDate?, emoji: AnniversaryEmoji?) {
        title?.let { this.title = it }
        anniversaryDate?.let { this.anniversaryDate = it }
        emoji?.let { this.emoji = it }
    }
}
