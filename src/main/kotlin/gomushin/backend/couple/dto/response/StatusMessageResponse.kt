package gomushin.backend.couple.dto.response

data class StatusMessageResponse (
    val statusMessage : String?
) {
    companion object {
        fun of(
            statusMessage: String?
        )=StatusMessageResponse(
            statusMessage
        )
    }
}