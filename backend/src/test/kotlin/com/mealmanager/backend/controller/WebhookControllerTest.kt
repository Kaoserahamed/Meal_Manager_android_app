package com.mealmanager.backend.controller

import com.mealmanager.backend.service.WebhookService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class WebhookControllerTest {

    private lateinit var controller: WebhookController

    @BeforeEach
    fun setUp() { controller = WebhookController(WebhookService()) }

    @Test
    fun `createEndpoint returns 200`() {
        val response = controller.createEndpoint(com.mealmanager.backend.model.dto.WebhookEndpointDto(url = "https://example.com"))
        assertEquals(200, response.statusCode.value())
    }

    @Test
    fun `listEndpoints returns 200`() {
        val response = controller.listEndpoints()
        assertEquals(200, response.statusCode.value())
    }
}
