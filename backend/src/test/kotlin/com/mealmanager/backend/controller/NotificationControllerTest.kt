package com.mealmanager.backend.controller

import com.mealmanager.backend.service.NotificationService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class NotificationControllerTest {

    private lateinit var controller: NotificationController

    @BeforeEach
    fun setUp() { controller = NotificationController(NotificationService()) }

    @Test
    fun `sendNotification returns 200`() {
        val response = controller.sendNotification(com.mealmanager.backend.model.dto.NotificationDto(title = "Hi", body = "There"))
        assertEquals(200, response.statusCode.value())
    }

    @Test
    fun `listNotifications returns 200`() {
        val response = controller.listNotifications(0, 20)
        assertEquals(200, response.statusCode.value())
    }

    @Test
    fun `getPreferences returns 200`() {
        val response = controller.getPreferences()
        assertEquals(200, response.statusCode.value())
    }
}
