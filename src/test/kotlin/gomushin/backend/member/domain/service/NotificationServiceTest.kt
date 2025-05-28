package gomushin.backend.member.domain.service

import gomushin.backend.member.domain.entity.Notification
import gomushin.backend.member.domain.repository.NotificationRepository
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
class NotificationServiceTest {

    @Mock
    private lateinit var notificationRepository: NotificationRepository

    @InjectMocks
    private lateinit var notificationService: NotificationService

    @DisplayName("알림 초기화 성공 케이스")
    @Test
    fun initNotification_success() {
        // given
        val memberId = 1L
        val isNotification = true
        val notification = Notification.create(memberId).apply {
            dday = true
            partnerStatus = true
        }

        // when
        `when`(notificationRepository.save(any())).thenReturn(notification)
        notificationService.initNotification(memberId, isNotification)

        // then
        assertEquals(notification.dday, true)
        assertEquals(notification.partnerStatus, true)

    }
}
