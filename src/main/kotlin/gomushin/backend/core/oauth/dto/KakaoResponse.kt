package gomushin.backend.core.oauth.dto

import org.apache.coyote.BadRequestException

class KakaoResponse(private val attributes: Map<String, Any>) : OAuth2Response {
    override fun getProviderId(): String {
        return attributes["id"].toString()
    }

    override fun getEmail(): String {
        val kakaoAccount = attributes["kakao_account"] as? Map<*, *>

        return kakaoAccount?.get("email")?.toString()
            ?: throw BadRequestException("sarangggun.oauth.missing-email")
    }

    override fun getName(): String {
        val properties = attributes["properties"] as? Map<*, *>
        properties?.get("nickname")?.let {
            return it.toString()
        }

        val kakaoAccount = attributes["kakao_account"] as? Map<*, *>
        val profile = kakaoAccount?.get("profile") as? Map<*, *>
        return profile?.get("nickname")?.toString()
            ?: throw BadRequestException("sarangggun.oauth.missing-nickname")
    }

    fun getProfileImage(): String? {
        val kakaoAccount = attributes["kakao_account"] as? Map<*, *>
        val profile = kakaoAccount?.get("profile") as? Map<*, *>
        return profile?.get("profile_image_url") as? String
    }
}
