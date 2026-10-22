package com.mealmanager.backend.service

import com.mealmanager.backend.model.dto.Page
import com.mealmanager.backend.model.dto.PaymentDto
import com.mealmanager.backend.model.dto.PaymentIntentDto
import com.mealmanager.backend.model.dto.PaymentMethodDto
import com.mealmanager.backend.model.dto.RefundDto
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class PaymentServiceTest {

    private lateinit var service: PaymentService

    @BeforeEach
    fun setUp() { service = PaymentService() }

    @Test
    fun `createPaymentIntent returns pending payment`() {
        val req = PaymentIntentDto(amount = 100.0, currency = "USD")
        val result = service.createPaymentIntent(req)
        assertEquals(100.0, result.amount)
        assertEquals("pending", result.status)
    }

    @Test
    fun `getPayment returns payment by id`() {
        val id = java.util.UUID.randomUUID()
        val result = service.getPayment(id)
        assertEquals(id, result.id)
    }

    @Test
    fun `refundPayment returns refunded payment`() {
        val id = java.util.UUID.randomUUID()
        val result = service.refundPayment(id, RefundDto(amount = 50.0, currency = "USD"))
        assertEquals("refunded", result.status)
    }

    @Test
    fun `listPayments returns empty page`() {
        val page = service.listPayments(0, 20)
        assertEquals(0, page.total)
    }

    @Test
    fun `getPaymentMethods returns default methods`() {
        val methods = service.getPaymentMethods()
        assertEquals(2, methods.size)
        assertTrue(methods.any { it.isDefault })
    }

    @Test
    fun `addPaymentMethod assigns id`() {
        val method = service.addPaymentMethod(PaymentMethodDto(type = "card", provider = "stripe"))
        assertNotNull(method.id)
    }
}
