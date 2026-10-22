package com.mealmanager.backend.controller

import com.mealmanager.backend.service.CalendarService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate

class CalendarControllerTest {

    private lateinit var controller: CalendarController

    @BeforeEach
    fun setUp() { controller = CalendarController(CalendarService()) }

    @Test
    fun `listEvents returns 200`() {
        val response = controller.listEvents(LocalDate.now(), LocalDate.now().plusDays(7))
        assertEquals(200, response.statusCode.value())
    }

    @Test
    fun `createEvent returns 200`() {
        val response = controller.createEvent(com.mealmanager.backend.model.dto.CalendarEventDto(title = "Event"))
        assertEquals(200, response.statusCode.value())
    }

    @Test
    fun `getEvent returns 200`() {
        val response = controller.getEvent(java.util.UUID.randomUUID())
        assertEquals(200, response.statusCode.value())
    }
}
