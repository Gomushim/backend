package gomushin.backend.core.event.dto

data class S3DeleteEvent(
    val pictureUrls: List<String>
)
