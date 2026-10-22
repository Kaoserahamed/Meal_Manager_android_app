package com.mealmanager.backend.controller

import com.mealmanager.backend.model.dto.ApiResponse
import com.mealmanager.backend.model.dto.DeliveryDto
import com.mealmanager.backend.model.dto.DeliveryAssignmentDto
import com.mealmanager.backend.service.DeliveryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid
import java.util.UUID

@RestController
@RequestMapping("/api/v1/delivery")
class DeliveryController(
    private val deliveryService: DeliveryService
) {

    @PostMapping
    fun createDelivery(@Valid @RequestBody dto: DeliveryDto): ResponseEntity<ApiResponse<DeliveryDto>> {
        val created = deliveryService.createDelivery(dto)
        return ResponseEntity.ok(ApiResponse.success(created))
    }

    @GetMapping("/{id}")
    fun getDelivery(@PathVariable id: UUID): ResponseEntity<ApiResponse<DeliveryDto>> {
        val delivery = deliveryService.getDelivery(id)
        return ResponseEntity.ok(ApiResponse.success(delivery))
    }

    @PutMapping("/{id}/status")
    fun updateDeliveryStatus(
        @PathVariable id: UUID,
        @Valid @RequestBody request: DeliveryStatusRequest
    ): ResponseEntity<ApiResponse<DeliveryDto>> {
        val updated = deliveryService.updateStatus(id, request.status)
        return ResponseEntity.ok(ApiResponse.success(updated))
    }

    @PostMapping("/{id}/assign")
    fun assignDriver(
        @PathVariable id: UUID,
        @Valid @RequestBody assignment: DeliveryAssignmentDto
    ): ResponseEntity<ApiResponse<DeliveryDto>> {
        val updated = deliveryService.assignDriver(id, assignment)
        return ResponseEntity.ok(ApiResponse.success(updated))
    }

    @GetMapping
    fun listDeliveries(
        @RequestParam(required = false) status: String?,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<ApiResponse<Page<DeliveryDto>>> {
        val result = deliveryService.listDeliveries(status, page, size)
        return ResponseEntity.ok(ApiResponse.success(result))
    }

    @GetMapping("/drivers")
    fun listDrivers(): ResponseEntity<ApiResponse<List<DriverDto>>> {
        val drivers = deliveryService.listDrivers()
        return ResponseEntity.ok(ApiResponse.success(drivers))
    }

    @GetMapping("/{id}/tracking")
    fun getTracking(@PathVariable id: UUID): ResponseEntity<ApiResponse<DeliveryTrackingDto>> {
        val tracking = deliveryService.getTracking(id)
        return ResponseEntity.ok(ApiResponse.success(tracking))
    }
}

data class DeliveryStatusRequest(val status: String)
data class DriverDto(
    val id: UUID? = null,
    val name: String = "",
    val phone: String = "",
    val vehicleType: String = "",
    val isAvailable: Boolean = true,
    val rating: Double = 0.0
)
data class DeliveryTrackingDto(
    val deliveryId: UUID = UUID.randomUUID(),
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val updatedAt: String = java.time.Instant.now().toString()
)
