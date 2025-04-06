package gomushin.backend.auth.presentation.response

data class LoginOAuthResponse(
    val success: Boolean,
    val userInfo: UserInfo? = null,
    val token: String?,
) {
    data class UserInfo(
        val id: String,
        val email: String?,
        val name: String?,
        val profileImageUrl: String?
    )

    companion object {
        fun success(token: String, userInfo: UserInfo): LoginOAuthResponse = LoginOAuthResponse(true, userInfo, token)
        fun error(message: String): LoginOAuthResponse = LoginOAuthResponse(false, null, null)
    }
}
