package gomushin.backend.auth.infrastructure.kakao

import com.fasterxml.jackson.annotation.JsonProperty
import gomushin.backend.auth.domain.oauth.OAuthUserInfo

data class KakaoOAuthUserInfo(
    val id: Long,
    @JsonProperty("connected_at")
    val connectedAt: String,
    val properties: KakaoProperties,
    @JsonProperty("kakao_account")
    val kakaoAccount: KakaoAccount
) {
    fun toOAuthUserInfo(): OAuthUserInfo {
        return OAuthUserInfo(
            id = this.id.toString(),
            email = this.kakaoAccount.email,
            name = this.properties.nickname,
            profileImageUrl = this.properties.profileImage
        )
    }
}

data class KakaoProperties(
    val nickname: String,

    @JsonProperty("profile_image")
    val profileImage: String,

    @JsonProperty("thumbnail_image")
    val thumbnailImage: String
)

data class KakaoAccount(
    @JsonProperty("profile_nickname_needs_agreement")
    val profileNicknameNeedsAgreement: Boolean,

    @JsonProperty("profile_image_needs_agreement")
    val profileImageNeedsAgreement: Boolean,

    @JsonProperty("has_email")
    val hasEmail: Boolean,

    @JsonProperty("email_needs_agreement")
    val emailNeedsAgreement: Boolean,

    @JsonProperty("is_email_valid")
    val isEmailValid: Boolean,

    @JsonProperty("is_email_verified")
    val isEmailVerified: Boolean,

    val email: String,

    val profile: KakaoProfile
)

data class KakaoProfile(
    val nickname: String,

    @JsonProperty("thumbnail_image_url")
    val thumbnailImageUrl: String,

    @JsonProperty("profile_image_url")
    val profileImageUrl: String,

    @JsonProperty("is_default_image")
    val isDefaultImage: Boolean,

    @JsonProperty("is_default_nickname")
    val isDefaultNickname: Boolean
)
