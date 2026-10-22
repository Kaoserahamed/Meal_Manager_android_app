package com.mealmanager.backend.service

import com.mealmanager.backend.model.dto.InvoiceDto
import com.mealmanager.backend.model.dto.SubscriptionDto
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

class SubscriptionServiceTest {

    private lateinit var service: SubscriptionService

    @BeforeEach
    fun setUp() { service = SubscriptionService() }

    @Test
    fun `getSubscription returns subscription`() {
        val userId = UUID.randomUUID()
        val sub = service.getSubscription(userId)
        assertEquals(userId, sub.userId)
    }

    @Test
    fun `createSubscription assigns id`() {
        val dto = SubscriptionDto(plan = "premium")
        val result = service.createSubscription(dto)
        assertNotNull(result.id)
    }

    @Test
    fun `cancelSubscription sets cancelled status`() {
        val id = UUID.randomUUID()
        val result = service.cancelSubscription(id)
        assertEquals("cancelled", result.status)
    }

    @Test
    fun `listInvoices returns empty page`() {
        val page = service.listInvoices(0, 20)
        assertEquals(0, page.total)
    }
}
