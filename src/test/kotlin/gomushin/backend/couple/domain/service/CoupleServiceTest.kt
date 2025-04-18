package gomushin.backend.couple.domain.service

import gomushin.backend.couple.domain.entity.Couple
import gomushin.backend.couple.domain.repository.CoupleRepository
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class CoupleServiceTest {

    @Mock
    private lateinit var coupleRepository: CoupleRepository

    @InjectMocks
    private lateinit var coupleService: CoupleService

    @DisplayName("getById - 성공")
    @Test
    fun getById_success() {
        // given
        val coupleId = 1L
        val couple = Couple(
            id = coupleId,
            invitorId = 1L,
            inviteeId = 2L,
        )

        `when`(coupleRepository.findById(coupleId)).thenReturn(Optional.of(couple))

        // when
        val result = coupleService.getById(coupleId)

        // then
        assert(result.id == coupleId)
        verify(coupleRepository).findById(coupleId)
    }

    @DisplayName("findById - 성공")
    @Test
    fun findById_success() {
        // given
        val coupleId = 1L
        val couple = Couple(
            id = coupleId,
            invitorId = 1L,
            inviteeId = 2L,
        )

        `when`(coupleRepository.findById(coupleId)).thenReturn(Optional.of(couple))

        // when
        val result = coupleService.findById(coupleId)

        // then
        assert(result?.id == coupleId)
        verify(coupleRepository).findById(coupleId)
    }

    @DisplayName("save - 성공")
    @Test
    fun save_success() {
        // given
        val couple = Couple(
            id = 1L,
            invitorId = 1L,
            inviteeId = 2L,
        )

        `when`(coupleRepository.save(couple)).thenReturn(couple)

        // when
        val result = coupleService.save(couple)

        // then
        assert(result.id == couple.id)
        verify(coupleRepository).save(couple)
    }
}
