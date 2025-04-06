package gomushin.backend.auth.domain.oauth

data class OAuthUserInfo(
    val id: String,
    val email: String?,
    val name: String?,
    val profileImageUrl: String?
)
