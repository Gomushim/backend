package gomushin.backend.member.domain.value

import gomushin.backend.core.infrastructure.exception.BadRequestException

enum class Provider(
    private val value: String
) {
    GOOGLE("google"),
    KAKAO("kakao"),
    NAVER("naver");

    companion object {
        fun getProviderByValue(value: String): Provider {
            return entries.firstOrNull { it.value == value }
                ?: throw BadRequestException("sarangggun.oauth.invalid-provider")
        }
    }
}
