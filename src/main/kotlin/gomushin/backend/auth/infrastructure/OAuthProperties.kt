package gomushin.backend.auth.infrastructure

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("oauth2")
data class OAuthProperties(
    val kakao: OAuthProvider
) {
    data class OAuthProvider(
        val authUri: String,
        val clientId: String,
        val clientSecret: String,
        val redirectUri: String,
        val tokenUri: String,
        val resourceUri: String,
        val scope: List<String>
    )
}
