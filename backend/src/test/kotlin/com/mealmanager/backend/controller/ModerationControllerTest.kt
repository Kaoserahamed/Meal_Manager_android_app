package com.mealmanager.backend.controller

import com.mealmanager.backend.service.ModerationService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ModerationControllerTest {

    private lateinit var controller: ModerationController

    @BeforeEach
    fun setUp() { controller = ModerationController(ModerationService()) }

    @Test
    fun `getQueue returns 200`() {
        val response = controller.getQueue(0, 20)
        assertEquals(200, response.statusCode.value())
    }

    @Test
    fun `takeAction returns 200`() {
        val response = controller.takeAction(com.mealmanager.backend.model.dto.ModerationActionDto(action = "approve"))
        assertEquals(200, response.statusCode.value())
    }
}
