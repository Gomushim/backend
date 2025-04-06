package gomushin.backend.auth.infrastructure.kakao

import gomushin.backend.auth.domain.oauth.OAuthProvider
import gomushin.backend.auth.domain.oauth.OAuthToken
import gomushin.backend.auth.domain.oauth.OAuthUserInfo
import gomushin.backend.auth.infrastructure.OAuthProperties
import gomushin.backend.core.infrastructure.exception.BadRequestException
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Component
class KakaoOAuthProvider(private val oAuthProperties: OAuthProperties) : OAuthProvider {
    override fun getAuthorizationUrl(): String {
        val kakaoProperties = oAuthProperties.kakao
        return "https://kauth.kakao.com/oauth/authorize?" +
                "client_id=${kakaoProperties.clientId}" +
                "&redirect_uri=${kakaoProperties.redirectUri}" +
                "&response_type=code"
    }


    override fun getUserInfo(accessToken: String): OAuthUserInfo {
        val resourceUri = oAuthProperties.kakao.resourceUri
        val restClient = RestClient.builder()
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
            .build()

        val responseString = restClient.get()
            .uri(resourceUri)
            .retrieve()
            .toEntity(String::class.java)

        println("responseString = $responseString")

        val response = restClient.get()
            .uri(resourceUri)
            .retrieve()
            .toEntity(KakaoOAuthUserInfo::class.java)

        val kakaoUserInfo = response.body ?: throw BadRequestException("saranggun.oauth.failed-to-fetch-user-info")

        return kakaoUserInfo.toOAuthUserInfo()
    }

    override fun getToken(code: String): OAuthToken {
        val kakaoProperties = oAuthProperties.kakao

        val params = mapOf(
            "grant_type" to "authorization_code",
            "client_id" to kakaoProperties.clientId,
            "redirect_uri" to kakaoProperties.redirectUri,
            "code" to code,
            "client_secret" to kakaoProperties.clientSecret
        )

        val restClient = RestClient.builder()
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=utf-8")
            .build()

        val tokenUrl = kakaoProperties.tokenUri

        val response = restClient.post()
            .uri(tokenUrl)
            .body(params.toFormData())
            .retrieve()
            .toEntity(OAuthToken::class.java)

        return response.body ?: throw IllegalStateException("카카오 토큰 페칭 실패")
    }

    private fun Map<String, String>.toFormData(): String {
        return this.entries.joinToString("&") {
            "${
                URLEncoder.encode(
                    it.key,
                    StandardCharsets.UTF_8
                )
            }=${URLEncoder.encode(it.value, StandardCharsets.UTF_8)}"
        }
    }
}
