package gomushin.backend.couple.domain.value

import gomushin.backend.core.infrastructure.exception.BadRequestException

enum class Military {
    ARMY,
    NAVY,
    AIR_FORCE,
    MARINE;

    companion object {
        fun getByName(name: String): Military {
            return entries.firstOrNull { it.name == name }
                ?: throw BadRequestException("sarangggun.military.not-exist-military")
        }
    }
}
