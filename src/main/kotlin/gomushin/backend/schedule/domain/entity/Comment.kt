package gomushin.backend.schedule.domain.entity

import gomushin.backend.core.infrastructure.jpa.shared.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "comment")
class Comment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(name = "letter_id", nullable = false)
    val letterId: Long = 0L,

    @Column(name = "author_id", nullable = false)
    val authorId: Long = 0L,

    @Column(name = "nickname", nullable = false)
    var nickname: String = "",

    @Column(name = "content", nullable = false)
    var content: String = "",
) : BaseEntity() {
    companion object {
        fun of(
            letterId: Long,
            authorId: Long,
            nickname: String,
            content: String,
        ): Comment {
            return Comment(
                letterId = letterId,
                authorId = authorId,
                nickname = nickname,
                content = content,
            )
        }
    }
}
