package gomushin.backend.core.jwt

interface JwtTokenProvider {
    fun provideAccessToken(userId: Long, role: String): String
    fun getMemberIdFromToken(token: String): Long
    fun validateToken(token: String): Boolean
}
