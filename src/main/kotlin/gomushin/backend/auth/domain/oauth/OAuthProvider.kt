package gomushin.backend.auth.domain.oauth

interface OAuthProvider {
    fun getAuthorizationUrl(): String
    fun getToken(code: String): OAuthToken
    fun getUserInfo(accessToken: String): OAuthUserInfo
}
