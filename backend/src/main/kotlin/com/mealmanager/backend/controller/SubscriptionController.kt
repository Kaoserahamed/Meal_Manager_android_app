package com.mealmanager.backend.controller

import com.mealmanager.backend.model.dto.ApiResponse
import com.mealmanager.backend.model.dto.SubscriptionDto
import com.mealmanager.backend.model.dto.InvoiceDto
import com.mealmanager.backend.service.SubscriptionService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import jakarta.validation.Valid
import java.util.UUID

@RestController
@RequestMapping("/api/v1/subscriptions")
class SubscriptionController(
    private val subscriptionService: SubscriptionService
) {

    @GetMapping("/{userId}")
    fun getSubscription(@PathVariable userId: UUID): ResponseEntity<ApiResponse<SubscriptionDto>> {
        val sub = subscriptionService.getSubscription(userId)
        return ResponseEntity.ok(ApiResponse.success(sub))
    }

    @PostMapping
    fun createSubscription(@Valid @RequestBody dto: SubscriptionDto): ResponseEntity<ApiResponse<SubscriptionDto>> {
        val created = subscriptionService.createSubscription(dto)
        return ResponseEntity.ok(ApiResponse.success(created))
    }

    @PostMapping("/{id}/cancel")
    fun cancelSubscription(@PathVariable id: UUID): ResponseEntity<ApiResponse<SubscriptionDto>> {
        val updated = subscriptionService.cancelSubscription(id)
        return ResponseEntity.ok(ApiResponse.success(updated))
    }

    @GetMapping("/invoices")
    fun listInvoices(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<ApiResponse<Page<InvoiceDto>>> {
        val result = subscriptionService.listInvoices(page, size)
        return ResponseEntity.ok(ApiResponse.success(result))
    }

    @PostMapping("/webhook/stripe")
    fun stripeWebhook(@RequestBody payload: String): ResponseEntity<ApiResponse<Void>> {
        subscriptionService.handleStripeWebhook(payload)
        return ResponseEntity.ok(ApiResponse.success(null))
    }
}
