package gomushin.backend.auth.presentation

object ApiPath {
    object OAuth {
        const val AUTHORIZE_OAUTH = "/v1/oauth/{provider}"
        const val LOGIN_OAUTH = "/v1/oauth/login/{provider}"
    }
}
