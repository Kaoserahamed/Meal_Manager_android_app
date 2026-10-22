package com.mealmanager.backend.controller

import com.mealmanager.backend.service.SubscriptionService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

class SubscriptionControllerTest {

    private lateinit var controller: SubscriptionController

    @BeforeEach
    fun setUp() { controller = SubscriptionController(SubscriptionService()) }

    @Test
    fun `getSubscription returns 200`() {
        val response = controller.getSubscription(UUID.randomUUID())
        assertEquals(200, response.statusCode.value())
    }

    @Test
    fun `listInvoices returns 200`() {
        val response = controller.listInvoices(0, 20)
        assertEquals(200, response.statusCode.value())
    }
}
