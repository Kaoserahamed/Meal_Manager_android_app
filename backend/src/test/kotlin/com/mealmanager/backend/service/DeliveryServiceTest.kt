package com.mealmanager.backend.service

import com.mealmanager.backend.model.dto.DeliveryDto
import com.mealmanager.backend.model.dto.DriverDto
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DeliveryServiceTest {

    private lateinit var service: DeliveryService

    @BeforeEach
    fun setUp() { service = DeliveryService() }

    @Test
    fun `createDelivery sets pending status`() {
        val dto = DeliveryDto(address = "123 Main St")
        val result = service.createDelivery(dto)
        assertEquals("pending", result.status)
    }

    @Test
    fun `getDelivery returns delivery`() {
        val id = java.util.UUID.randomUUID()
        val result = service.getDelivery(id)
        assertEquals(id, result.id)
    }

    @Test
    fun `updateStatus changes status`() {
        val id = java.util.UUID.randomUUID()
        val result = service.updateStatus(id, "delivered")
        assertEquals("delivered", result.status)
    }

    @Test
    fun `assignDriver sets driver`() {
        val id = java.util.UUID.randomUUID()
        val result = service.assignDriver(id, com.mealmanager.backend.model.dto.DeliveryAssignmentDto(driverId = java.util.UUID.randomUUID()))
        assertNotNull(result.driverId)
    }

    @Test
    fun `listDrivers returns drivers`() {
        val drivers = service.listDrivers()
        assertEquals(2, drivers.size)
        assertTrue(drivers.all { it.rating >= 4.0 })
    }

    @Test
    fun `getTracking returns coordinates`() {
        val id = java.util.UUID.randomUUID()
        val tracking = service.getTracking(id)
        assertEquals(id, tracking.deliveryId)
    }
}
