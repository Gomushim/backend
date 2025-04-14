package gomushin.backend.core.oauth.service

import gomushin.backend.core.oauth.dto.KakaoResponse
import gomushin.backend.core.oauth.dto.UserDTO
import gomushin.backend.core.infrastructure.exception.BadRequestException
import gomushin.backend.core.oauth.CustomOAuth2User
import gomushin.backend.member.domain.entity.Member
import gomushin.backend.member.domain.repository.MemberRepository
import gomushin.backend.member.domain.value.Provider
import gomushin.backend.member.domain.value.Role
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CustomOAuth2UserService(
    private val memberRepository: MemberRepository
) : DefaultOAuth2UserService() {

    @Transactional
    override fun loadUser(oauth2UserRequest: OAuth2UserRequest): OAuth2User? {
        val oAuth2User = super.loadUser(oauth2UserRequest)

        val registrationId = oauth2UserRequest.clientRegistration.registrationId
        val oAuth2Response = when (registrationId) {
            "kakao" -> KakaoResponse(oAuth2User.attributes)
            else -> throw BadRequestException("sarangggun.oauth.invalid-provider")
        }

        val email = oAuth2Response.getEmail()

        getMemberByEmail(email)?.let {
            it.email = oAuth2Response.getEmail()
            it.nickname = oAuth2Response.getName()
            it.profileImageUrl = oAuth2Response.getProfileImage()

            val savedMember = memberRepository.save(it)

            val userDto = UserDTO(
                username = oAuth2Response.getProviderId(),
                name = oAuth2Response.getName(),
                email = oAuth2Response.getEmail(),
                profileImage = oAuth2Response.getProfileImage(),
                role = Role.MEMBER.name,
                registrationId = registrationId,
                userId = savedMember.id,
            )

            return CustomOAuth2User(userDto)
        } ?: run {
            val newMember = Member.create(
                nickname = oAuth2Response.getName(),
                email = oAuth2Response.getEmail(),
                profileImageUrl = oAuth2Response.getProfileImage(),
                provider = Provider.getProviderByValue(registrationId)
            )

            val savedMember = memberRepository.save(newMember)

            val userDto = UserDTO(
                username = oAuth2Response.getProviderId(),
                name = oAuth2Response.getName(),
                email = oAuth2Response.getEmail(),
                profileImage = oAuth2Response.getProfileImage(),
                role = Role.MEMBER.name,
                registrationId = registrationId,
                userId = savedMember.id,
            )
            return CustomOAuth2User(userDto)
        }
    }

    private fun getMemberByEmail(email: String): Member? {
        return memberRepository.findByEmail(email)
    }
}
