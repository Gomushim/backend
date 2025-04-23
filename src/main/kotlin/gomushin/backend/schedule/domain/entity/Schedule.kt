package gomushin.backend.schedule.domain.entity

import gomushin.backend.core.infrastructure.jpa.shared.BaseEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "schedule")
class Schedule(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(name = "couple_id", nullable = false)
    val coupleId: Long = 0L,

    @Column(name = "user_id", nullable = false)
    val userId: Long = 0L,

    @Column(name = "content", nullable = false)
    var content: String,

    @Column(name = "start_date", nullable = false)
    var startDate: LocalDateTime,

    @Column(name = "end_date", nullable = false)
    var endDate: LocalDateTime,

    @Column(name = "is_all_day", nullable = false)
    var isAllDay: Boolean = false,

    @Column(name = "fatigue", nullable = false)
    var fatigue: String,
) : BaseEntity() {
    companion object {
        fun of(
            coupleId: Long,
            userId: Long,
            content: String,
            startDate: LocalDateTime,
            endDate: LocalDateTime,
            fatigue: String,
            isAllDay: Boolean?,
        ): Schedule {
            return Schedule(
                coupleId = coupleId,
                userId = userId,
                content = content,
                startDate = startDate,
                endDate = endDate,
                fatigue = fatigue,
                isAllDay = isAllDay ?: false,
            )
        }
    }
}
