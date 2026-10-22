package com.mealmanager.backend.service

import com.mealmanager.backend.model.dto.Page
import com.mealmanager.backend.model.dto.DeliveryDto
import com.mealmanager.backend.model.dto.DeliveryAssignmentDto
import com.mealmanager.backend.model.dto.DriverDto
import com.mealmanager.backend.model.dto.DeliveryTrackingDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional
class DeliveryService {

    fun createDelivery(dto: DeliveryDto): DeliveryDto {
        return dto.copy(id = UUID.randomUUID(), status = "pending")
    }

    fun getDelivery(id: UUID): DeliveryDto {
        return DeliveryDto(
            id = id,
            orderId = UUID.randomUUID(),
            driverId = null,
            status = "delivered",
            address = "123 Main St",
            createdAt = java.time.Instant.now().toString()
        )
    }

    fun updateStatus(id: UUID, status: String): DeliveryDto {
        return DeliveryDto(id = id, status = status)
    }

    fun assignDriver(id: UUID, assignment: DeliveryAssignmentDto): DeliveryDto {
        return DeliveryDto(
            id = id,
            driverId = assignment.driverId,
            status = "assigned"
        )
    }

    fun listDeliveries(status: String?, page: Int, size: Int): Page<DeliveryDto> {
        return Page(content = emptyList(), page = page, size = size, total = 0)
    }

    fun listDrivers(): List<DriverDto> {
        return listOf(
            DriverDto(name = "John Doe", vehicleType = "car", rating = 4.8),
            DriverDto(name = "Jane Smith", vehicleType = "bike", rating = 4.9)
        )
    }

    fun getTracking(id: UUID): DeliveryTrackingDto {
        return DeliveryTrackingDto(deliveryId = id, lat = 40.7128, lng = -74.0060)
    }
}
