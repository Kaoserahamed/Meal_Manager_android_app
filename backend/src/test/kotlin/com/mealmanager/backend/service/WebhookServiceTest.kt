package com.mealmanager.backend.service

import com.mealmanager.backend.model.dto.WebhookEndpointDto
import com.mealmanager.backend.model.dto.WebhookDeliveryDto
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class WebhookServiceTest {

    private lateinit var service: WebhookService

    @BeforeEach
    fun setUp() { service = WebhookService() }

    @Test
    fun `createEndpoint assigns id`() {
        val dto = WebhookEndpointDto(url = "https://example.com/webhook")
        val result = service.createEndpoint(dto)
        assertNotNull(result.id)
    }

    @Test
    fun `listEndpoints returns empty list`() {
        assertTrue(service.listEndpoints().isEmpty())
    }

    @Test
    fun `deliver returns delivery`() {
        val id = java.util.UUID.randomUUID()
        val delivery = service.deliver(id)
        assertEquals(id, delivery.endpointId)
        assertEquals("delivered", delivery.status)
    }

    @Test
    fun `listDeliveries returns empty page`() {
        val page = service.listDeliveries(0, 20)
        assertEquals(0, page.total)
    }
}
