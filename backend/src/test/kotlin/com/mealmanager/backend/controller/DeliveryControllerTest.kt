package com.mealmanager.backend.controller

import com.mealmanager.backend.service.DeliveryService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DeliveryControllerTest {

    private lateinit var controller: DeliveryController

    @BeforeEach
    fun setUp() { controller = DeliveryController(DeliveryService()) }

    @Test
    fun `createDelivery returns 200`() {
        val response = controller.createDelivery(com.mealmanager.backend.model.dto.DeliveryDto(address = "123 St"))
        assertEquals(200, response.statusCode.value())
    }

    @Test
    fun `getDelivery returns 200`() {
        val response = controller.getDelivery(java.util.UUID.randomUUID())
        assertEquals(200, response.statusCode.value())
    }

    @Test
    fun `listDeliveries returns 200`() {
        val response = controller.listDeliveries(null, 0, 20)
        assertEquals(200, response.statusCode.value())
    }

    @Test
    fun `listDrivers returns 200`() {
        val response = controller.listDrivers()
        assertEquals(200, response.statusCode.value())
    }
}
