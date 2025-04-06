package gomushin.backend.auth.presentation.response

import com.fasterxml.jackson.annotation.JsonProperty

data class KakaoOAuthResponse(
    @JsonProperty("access_token")
    val accessToken: String,

    @JsonProperty("token_type")
    val tokenType: String,

    @JsonProperty("id_token")
    val idToken: String,

    @JsonProperty("refresh_token")
    val refreshToken: String,

    @JsonProperty("expires_in")
    val expiresIn: Int,

    @JsonProperty("refresh_token_expires_in")
    val refreshTokenExpiresIn: Int,

    @JsonProperty("scope")
    val scope: String
)
