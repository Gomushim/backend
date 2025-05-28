package gomushin.backend.couple.dto.request

data class ReadAnniversariesRequest(
    val key: Long = Long.MAX_VALUE,
    val orderCreatedAt: String = "DESC",
    val take: Long = 10L,
)
