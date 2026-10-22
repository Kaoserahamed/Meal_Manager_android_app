package com.mealmanager.backend.service

import com.mealmanager.backend.model.dto.NotificationDto
import com.mealmanager.backend.model.dto.NotificationPreferenceDto
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class NotificationServiceTest {

    private lateinit var service: NotificationService

    @BeforeEach
    fun setUp() { service = NotificationService() }

    @Test
    fun `sendNotification marks sent`() {
        val dto = NotificationDto(title = "Hello", body = "World")
        val result = service.sendNotification(dto)
        assertEquals("sent", result.status)
    }

    @Test
    fun `markRead does not throw`() {
        service.markRead(java.util.UUID.randomUUID())
    }

    @Test
    fun `updatePreferences returns preferences`() {
        val prefs = NotificationPreferenceDto(emailEnabled = false, pushEnabled = true, smsEnabled = true)
        val result = service.updatePreferences(prefs)
        assertFalse(result.emailEnabled)
        assertTrue(result.pushEnabled)
    }

    @Test
    fun `getPreferences returns defaults`() {
        val prefs = service.getPreferences()
        assertTrue(prefs.emailEnabled)
        assertTrue(prefs.pushEnabled)
    }
}
