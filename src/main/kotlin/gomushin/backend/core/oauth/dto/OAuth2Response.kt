package gomushin.backend.core.oauth.dto

interface OAuth2Response {
    fun getProviderId(): String

    fun getEmail(): String

    fun getName(): String
}
