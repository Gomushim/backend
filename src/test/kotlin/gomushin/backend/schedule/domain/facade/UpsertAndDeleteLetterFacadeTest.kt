package gomushin.backend.schedule.domain.facade

import gomushin.backend.core.CustomUserDetails
import gomushin.backend.core.service.S3Service
import gomushin.backend.schedule.domain.entity.Letter
import gomushin.backend.schedule.domain.service.LetterService
import gomushin.backend.schedule.domain.service.PictureService
import gomushin.backend.schedule.dto.UpsertLetterRequest
import gomushin.backend.schedule.facade.UpsertAndDeleteLetterFacade
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.web.multipart.MultipartFile

@ExtendWith(MockitoExtension::class)
class UpsertAndDeleteLetterFacadeTest {

    @Mock
    private lateinit var letterService: LetterService

    @Mock
    private lateinit var s3Service: S3Service

    @Mock
    private lateinit var pictureService: PictureService

    @InjectMocks
    private lateinit var upsertAndDeleteLetterFacade: UpsertAndDeleteLetterFacade


    @Nested
    inner class Upsert {

        @DisplayName("업로드 성공 - 업로드된 사진이 있을 때")
        @Test
        fun upsertWithPictures_success() {
            // given
            val customUserDetails = mock(CustomUserDetails::class.java)
            val upsertLetterRequest = UpsertLetterRequest(
                title = "제목",
                content = "내용",
                scheduleId = 1L
            )
            val pictures = listOf(mock(MultipartFile::class.java))

            val letter = mock(Letter::class.java)

            // when
            `when`(letter.id).thenReturn(1L)
            `when`(customUserDetails.getId()).thenReturn(1L)
            `when`(letterService.upsert(1L, upsertLetterRequest)).thenReturn(letter)
            `when`(s3Service.uploadFile(pictures[0])).thenReturn("http://example.com/test.jpg")
            doNothing().`when`(pictureService).upsert(1L, listOf("http://example.com/test.jpg"))
            upsertAndDeleteLetterFacade.upsert(customUserDetails, upsertLetterRequest, pictures)

            // then
            verify(
                letterService,
                times(1)
            ).upsert(1L, upsertLetterRequest)
        }

        @DisplayName("업로드 성공 - 업로드된 사진이 없을 때")
        @Test
        fun upsertWithoutPictures_success() {
            // given
            val customUserDetails = mock(CustomUserDetails::class.java)
            val upsertLetterRequest = UpsertLetterRequest(
                title = "제목",
                content = "내용",
                scheduleId = 1L
            )
            val pictures: List<MultipartFile>? = null
            val letter = mock(Letter::class.java)

            // when
            `when`(customUserDetails.getId()).thenReturn(1L)
            `when`(letterService.upsert(1L, upsertLetterRequest)).thenReturn(letter)
            upsertAndDeleteLetterFacade.upsert(customUserDetails, upsertLetterRequest, pictures)

            // then
            verify(letterService, times(1)).upsert(1L, upsertLetterRequest)
            verify(s3Service, never()).uploadFile(org.mockito.kotlin.any())
            verify(pictureService, never()).upsert(org.mockito.kotlin.any(), org.mockito.kotlin.any())
        }
    }

    @DisplayName("삭제 성공")
    @Test
    fun delete_success() {
        // given
        val customUserDetails = mock(CustomUserDetails::class.java)
        val scheduleId = 1L
        val letterId = 1L
        val mockLetter = mock(Letter::class.java)

        // when
        `when`(letterService.getById(letterId)).thenReturn(mockLetter)
        `when`(customUserDetails.getId()).thenReturn(1L)
        `when`(mockLetter.authorId).thenReturn(1L)
        `when`(mockLetter.scheduleId).thenReturn(scheduleId)
        upsertAndDeleteLetterFacade.delete(
            customUserDetails,
            scheduleId,
            letterId
        )

        // then
        verify(letterService, times(1)).getById(letterId)
        verify(letterService, times(1)).delete(letterId)
        verify(pictureService, times(1)).deleteAllByLetterId(letterId)

    }
}
