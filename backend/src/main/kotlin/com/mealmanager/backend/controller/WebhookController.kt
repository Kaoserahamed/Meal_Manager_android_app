package com.mealmanager.backend.controller

import com.mealmanager.backend.model.dto.ApiResponse
import com.mealmanager.backend.model.dto.WebhookEndpointDto
import com.mealmanager.backend.model.dto.WebhookDeliveryDto
import com.mealmanager.backend.service.WebhookService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid
import java.util.UUID

@RestController
@RequestMapping("/api/v1/webhooks")
class WebhookController(
    private val webhookService: WebhookService
) {

    @PostMapping("/endpoints")
    fun createEndpoint(@Valid @RequestBody dto: WebhookEndpointDto): ResponseEntity<ApiResponse<WebhookEndpointDto>> {
        val created = webhookService.createEndpoint(dto)
        return ResponseEntity.ok(ApiResponse.success(created))
    }

    @GetMapping("/endpoints")
    fun listEndpoints(): ResponseEntity<ApiResponse<List<WebhookEndpointDto>>> {
        val endpoints = webhookService.listEndpoints()
        return ResponseEntity.ok(ApiResponse.success(endpoints))
    }

    @DeleteMapping("/endpoints/{id}")
    fun deleteEndpoint(@PathVariable id: UUID): ResponseEntity<ApiResponse<Void>> {
        webhookService.deleteEndpoint(id)
        return ResponseEntity.ok(ApiResponse.success(null))
    }

    @PostMapping("/endpoints/{id}/deliver")
    fun deliver(@PathVariable id: UUID): ResponseEntity<ApiResponse<WebhookDeliveryDto>> {
        val delivery = webhookService.deliver(id)
        return ResponseEntity.ok(ApiResponse.success(delivery))
    }

    @GetMapping("/deliveries")
    fun listDeliveries(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<ApiResponse<Page<WebhookDeliveryDto>>> {
        val result = webhookService.listDeliveries(page, size)
        return ResponseEntity.ok(ApiResponse.success(result))
    }

    @PostMapping("/ingest/{endpointId}")
    fun ingest(@PathVariable endpointId: UUID, @RequestBody payload: String): ResponseEntity<ApiResponse<Void>> {
        webhookService.ingest(endpointId, payload)
        return ResponseEntity.ok(ApiResponse.success(null))
    }
}
