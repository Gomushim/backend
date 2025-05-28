package gomushin.backend.core.event.listener

import gomushin.backend.core.service.S3Service
import gomushin.backend.core.event.dto.S3DeleteEvent
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class S3PicturesDeleteEventListener(
    private val s3Service: S3Service,
) {

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handle(event: S3DeleteEvent) {
        event.pictureUrls.forEach { pictureUrl ->
            s3Service.deleteFile(pictureUrl)
        }
    }
}
