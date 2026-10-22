package com.mealmanager.backend.service

import com.mealmanager.backend.model.dto.ModerationQueueDto
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

class ModerationServiceTest {

    private lateinit var service: ModerationService

    @BeforeEach
    fun setUp() { service = ModerationService() }

    @Test
    fun `getQueue returns empty page`() {
        val page = service.getQueue(0, 20)
        assertEquals(0, page.total)
    }

    @Test
    fun `takeAction does not throw`() {
        service.takeAction(com.mealmanager.backend.controller.ModerationActionDto(action = "approve"))
    }

    @Test
    fun `getReview returns review`() {
        val id = UUID.randomUUID()
        val review = service.getReview(id)
        assertEquals(id, review.id)
    }
}
