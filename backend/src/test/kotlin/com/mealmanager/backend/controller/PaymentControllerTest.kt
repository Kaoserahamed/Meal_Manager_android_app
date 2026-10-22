package com.mealmanager.backend.controller

import com.mealmanager.backend.service.PaymentService
import com.mealmanager.backend.model.dto.PaymentIntentDto
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.ResponseEntity

class PaymentControllerTest {

    private lateinit var controller: PaymentController

    @BeforeEach
    fun setUp() { controller = PaymentController(PaymentService()) }

    @Test
    fun `createPaymentIntent returns 200`() {
        val response = controller.createPaymentIntent(PaymentIntentDto(amount = 50.0))
        assertEquals(200, response.statusCode.value())
    }

    @Test
    fun `getPayment returns 200`() {
        val response = controller.getPayment(java.util.UUID.randomUUID())
        assertEquals(200, response.statusCode.value())
    }

    @Test
    fun `refundPayment returns 200`() {
        val response = controller.refundPayment(java.util.UUID.randomUUID(), com.mealmanager.backend.model.dto.RefundDto(amount = 20.0))
        assertEquals(200, response.statusCode.value())
    }

    @Test
    fun `listPayments returns 200`() {
        val response = controller.listPayments(0, 20)
        assertEquals(200, response.statusCode.value())
    }

    @Test
    fun `getPaymentMethods returns 200`() {
        val response = controller.getPaymentMethods()
        assertEquals(200, response.statusCode.value())
    }
}
