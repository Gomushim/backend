package gomushin.backend.core.oauth.dto

data class UserDTO(
    val registrationId: String,

    val role: String,

    val name: String,

    val username: String,

    val email: String? = null,

    val profileImage: String? = null,

    val userId: Long,
)
