package gomushin.backend.schedule.domain.entity

import gomushin.backend.core.infrastructure.jpa.shared.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "letter")
class Letter(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(name = "schedule_id", nullable = false)
    val scheduleId: Long = 0L,

    @Column(name = "author_id", nullable = false)
    val authorId: Long = 0L,

    @Column(name = "title", nullable = false)
    var title: String = "",

    @Column(name = "content", nullable = false)
    var content: String = "",

    ) : BaseEntity() {
    companion object {
        fun of(
            scheduleId: Long,
            authorId: Long,
            title: String,
            content: String,
        ): Letter {
            return Letter(
                scheduleId = scheduleId,
                authorId = authorId,
                title = title,
                content = content,
            )
        }
    }
}
