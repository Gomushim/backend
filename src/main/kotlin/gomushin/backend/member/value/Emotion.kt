package gomushin.backend.member.value

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import gomushin.backend.core.infrastructure.exception.BadRequestException

enum class Emotion (val code: Int) {
    MISS(1),
    GOOD(2),
    NOTHING(3),
    TIRED(4),
    SAD(5),
    WORRY(6),
    ANNOY(7);

    companion object {
        @JvmStatic
        @JsonCreator
        fun from(code: Int): Emotion =
            entries.find { it.code == code }
                ?: throw BadRequestException("sarangggun.member.not-exist-emoji")
    }

    @JsonValue
    fun toValue(): Int = code
}