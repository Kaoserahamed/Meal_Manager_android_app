package com.mealmanager.backend.service

import com.mealmanager.backend.model.dto.CalendarEventDto
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate

class CalendarServiceTest {

    private lateinit var service: CalendarService

    @BeforeEach
    fun setUp() { service = CalendarService() }

    @Test
    fun `listEvents returns empty list`() {
        val events = service.listEvents(LocalDate.now(), LocalDate.now().plusDays(7))
        assertTrue(events.isEmpty())
    }

    @Test
    fun `createEvent assigns id`() {
        val dto = CalendarEventDto(title = "Lunch")
        val result = service.createEvent(dto)
        assertNotNull(result.id)
    }

    @Test
    fun `getEvent returns event`() {
        val id = java.util.UUID.randomUUID()
        val result = service.getEvent(id)
        assertEquals(id, result.id)
    }

    @Test
    fun `updateEvent preserves id`() {
        val id = java.util.UUID.randomUUID()
        val result = service.updateEvent(id, CalendarEventDto(title = "Dinner"))
        assertEquals(id, result.id)
    }
}
