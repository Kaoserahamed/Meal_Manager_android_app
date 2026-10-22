package com.mealmanager.backend.service

import com.mealmanager.backend.model.dto.Page
import com.mealmanager.backend.model.dto.NotificationDto
import com.mealmanager.backend.model.dto.NotificationPreferenceDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional
class NotificationService {

    fun sendNotification(dto: NotificationDto): NotificationDto {
        return dto.copy(id = UUID.randomUUID(), status = "sent")
    }

    fun broadcast(request: com.mealmanager.backend.controller.BroadcastNotificationRequest) {
        // Broadcast notification to users
    }

    fun listNotifications(page: Int, size: Int): Page<NotificationDto> {
        return Page(content = emptyList(), page = page, size = size, total = 0)
    }

    fun markRead(id: UUID) {}

    fun updatePreferences(prefs: NotificationPreferenceDto): NotificationPreferenceDto {
        return prefs
    }

    fun getPreferences(): NotificationPreferenceDto {
        return NotificationPreferenceDto(
            emailEnabled = true,
            pushEnabled = true,
            smsEnabled = false
        )
    }

    fun registerPushToken(request: com.mealmanager.backend.controller.PushTokenRequest) {
        // Register FCM/APNS token
    }
}
