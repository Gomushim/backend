package gomushin.backend.schedule.domain.service

import gomushin.backend.schedule.domain.repository.PictureRepository
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.anyList
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.times
import kotlin.test.Test

@ExtendWith(MockitoExtension::class)
class PictureServiceTest {

    @Mock
    private lateinit var pictureRepository: PictureRepository

    @InjectMocks
    private lateinit var pictureService: PictureService

    @DisplayName("upsert 성공")
    @Test
    fun upsert_success() {
        // given
        val letterId = 1L
        val pictureUrls = listOf("http://example.com/test.jpg")

        // when
        pictureService.upsert(letterId, pictureUrls)

        // then
        verify(pictureRepository).deleteAllByLetterId(letterId)
        verify(pictureRepository, times(1)).saveAll(anyList())
    }
}
