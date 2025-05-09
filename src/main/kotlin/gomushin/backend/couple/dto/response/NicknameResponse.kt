package gomushin.backend.couple.dto.response

data class NicknameResponse (
    val userNickname : String,
    val coupleNickname : String
) {
    companion object {
        fun of(
            userNickname: String,
            coupleNickname: String
        ) = NicknameResponse(
            userNickname,
            coupleNickname
        )
    }
}
