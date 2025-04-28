package gomushin.backend.schedule.dto.request

data class ReadLettersToMePaginationRequest(
    val key: Long = Long.MAX_VALUE,
    val orderCreatedAt: String = "DESC",
    val take: Long = 10L,
)
