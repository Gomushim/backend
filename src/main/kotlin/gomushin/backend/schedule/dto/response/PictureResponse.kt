package gomushin.backend.schedule.dto.response

import gomushin.backend.schedule.domain.entity.Picture

data class PictureResponse(
    val id: Long,
    val pictureUrl: String,
    val letterId: Long,
) {
    companion object {
        fun of(picture: Picture): PictureResponse {
            return PictureResponse(
                id = picture.id,
                pictureUrl = picture.pictureUrl,
                letterId = picture.letterId,
            )
        }
    }
}

