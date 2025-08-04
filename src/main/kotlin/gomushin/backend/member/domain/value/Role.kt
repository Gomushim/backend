package gomushin.backend.member.domain.value

import gomushin.backend.core.infrastructure.exception.BadRequestException

enum class Role {
    GUEST,
    MEMBER;

    companion object {
        fun getByName(name: String): Role {
            return entries.firstOrNull { it.name == name }
                ?: throw BadRequestException("sarangggun.member.not-exist-role")
        }
    }
}
