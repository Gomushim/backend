package gomushin.backend.auth.application

import gomushin.backend.core.jwt.JwtTokenProvider
import gomushin.backend.auth.infrastructure.OAuthProviderFactory
import gomushin.backend.auth.presentation.response.LoginOAuthResponse
import gomushin.backend.member.domain.entity.Member
import gomushin.backend.member.domain.repository.MemberRepository
import gomushin.backend.member.domain.value.Provider
import org.apache.coyote.BadRequestException
import org.springframework.stereotype.Service
import java.util.*

@Service
class LoginOAuthUseCase(
    private val oAuthProviderFactory: OAuthProviderFactory,
    private val memberRepository: MemberRepository,
    private val jwtTokenProvider: JwtTokenProvider

) {
    fun execute(
        provider: String,
        code: String?,
        state: String?,
        error: String?,
        errorDescription: String?
    ): LoginOAuthResponse {
        val oAuthProvider = oAuthProviderFactory.getOAuthProvider(provider)
        when {
            code != null -> {
                val token = oAuthProvider.getToken(code)
                val userInfo = oAuthProvider.getUserInfo(token.accessToken)

                memberRepository.findByEmail(
                    userInfo.email ?: throw BadRequestException("sarangggun.member.not-exist-email")
                )?.let {
                    val provideAccessToken = jwtTokenProvider.provideAccessToken(it.id!!)

                    return LoginOAuthResponse.success(
                        provideAccessToken,
                        LoginOAuthResponse.UserInfo(it.id.toString(), it.email, it.name, it.profileImageUrl)
                    )
                }

                val savedMember = memberRepository.save(
                    Member.create(
                        userInfo.name ?: throw BadRequestException("sarangggun.member.not-exist-name"),
                        userInfo.email,
                        userInfo.profileImageUrl,
                        Provider.valueOf(provider.uppercase(Locale.getDefault()))
                    )
                )

                val provideAccessToken = jwtTokenProvider.provideAccessToken(savedMember.id!!)

                return LoginOAuthResponse.success(
                    provideAccessToken,
                    LoginOAuthResponse.UserInfo(
                        savedMember.id.toString(),
                        savedMember.email,
                        savedMember.name,
                        savedMember.profileImageUrl
                    )
                )
            }

            else -> {
                return LoginOAuthResponse.error("로그인 실패");
            }
        }
    }
}
