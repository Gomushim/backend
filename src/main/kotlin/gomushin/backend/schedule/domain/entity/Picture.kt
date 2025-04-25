package gomushin.backend.schedule.domain.entity

import jakarta.persistence.*

@Entity
@Table(name = "picture")
class Picture(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(name = "letter_id", nullable = false)
    val letterId: Long = 0L,

    @Column(name = "picture_url", nullable = false)
    val pictureUrl: String = "",
) {
    companion object {
        fun of(
            letterId: Long,
            pictureUrl: String,
        ): Picture {
            return Picture(
                letterId = letterId,
                pictureUrl = pictureUrl,
            )
        }
    }
}
