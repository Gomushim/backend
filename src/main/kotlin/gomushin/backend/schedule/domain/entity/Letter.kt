package gomushin.backend.schedule.domain.entity

import gomushin.backend.core.infrastructure.jpa.shared.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "letter")
class Letter(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(name = "couple_id", nullable = false)
    val coupleId: Long = 0L,

    @Column(name = "schedule_id", nullable = false)
    val scheduleId: Long = 0L,

    @Column(name = "author_id", nullable = false)
    val authorId: Long = 0L,

    @Column(name = "author", nullable = false)
    val author: String = "",

    @Column(name = "title", nullable = false)
    var title: String = "",

    @Column(name = "content", nullable = false)
    var content: String = "",

    ) : BaseEntity() {
    companion object {
        fun of(
            coupleId: Long,
            scheduleId: Long,
            authorId: Long,
            author: String,
            title: String,
            content: String,
        ): Letter {
            return Letter(
                coupleId = coupleId,
                scheduleId = scheduleId,
                authorId = authorId,
                author = author,
                title = title,
                content = content,
            )
        }
    }
}
