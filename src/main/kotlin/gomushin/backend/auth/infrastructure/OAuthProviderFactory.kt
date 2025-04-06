package gomushin.backend.auth.infrastructure

import gomushin.backend.auth.domain.oauth.OAuthProvider
import gomushin.backend.auth.infrastructure.kakao.KakaoOAuthProvider
import gomushin.backend.core.infrastructure.exception.BadRequestException
import org.springframework.stereotype.Component

@Component
class OAuthProviderFactory(
    private val kakaoOAuthProvider: KakaoOAuthProvider,
) {
    fun getOAuthProvider(provider: String): OAuthProvider {
        return when (provider) {
            "kakao" -> kakaoOAuthProvider
            else -> throw BadRequestException("sarangggun.oauth.invalid-provider")
        }
    }
}
