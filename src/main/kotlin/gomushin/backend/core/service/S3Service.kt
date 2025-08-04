package gomushin.backend.core.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.util.*

@Service
class S3Service(
    private val s3Client: S3Client,
    @Value("\${aws.s3.bucket}") private val bucket: String,
    @Value("\${aws.s3.base-url}") private val baseUrl: String,
) {

    fun uploadFile(multipartFile: MultipartFile): String {

        val fileName = generateFileName(multipartFile)

        s3Client.putObject(
            PutObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .contentType(multipartFile.contentType)
                .build(),
            RequestBody.fromInputStream(multipartFile.inputStream, multipartFile.size)
        )

        return getFileUrl(fileName)
    }

    fun deleteFile(fileName: String) {
        s3Client.deleteObject { it.bucket(bucket).key(getFileName(fileName)) }
    }

    private fun getFileName(fileUrl: String): String {
        return fileUrl.substringAfterLast("/")
    }

    private fun getFileUrl(fileName: String): String {
        val normalizedEndpoint = baseUrl.removeSuffix("/")
        return "$normalizedEndpoint/$bucket/$fileName"
    }

    private fun generateFileName(file: MultipartFile): String {
        val safeName = file.originalFilename?.replace(" ", "_") ?: "unknown-file"
        return "${UUID.randomUUID()}-$safeName"
    }
}
