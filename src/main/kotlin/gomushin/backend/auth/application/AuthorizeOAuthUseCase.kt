package gomushin.backend.auth.application

import gomushin.backend.auth.infrastructure.OAuthProviderFactory
import org.springframework.stereotype.Service

@Service
class AuthorizeOAuthUseCase(
    private val oAuthProviderFactory: OAuthProviderFactory
) {

    fun getRedirectUrl(provider: String): String {
        val oAuthProvider = oAuthProviderFactory.getOAuthProvider(provider)
        return oAuthProvider.getAuthorizationUrl()
    }
}
