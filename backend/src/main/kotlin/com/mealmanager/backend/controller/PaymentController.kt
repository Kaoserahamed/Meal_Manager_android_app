package com.mealmanager.backend.controller

import com.mealmanager.backend.model.dto.ApiResponse
import com.mealmanager.backend.model.dto.PaymentDto
import com.mealmanager.backend.model.dto.PaymentIntentDto
import com.mealmanager.backend.model.dto.RefundDto
import com.mealmanager.backend.service.PaymentService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid
import java.util.UUID

@RestController
@RequestMapping("/api/v1/payments")
class PaymentController(
    private val paymentService: PaymentService
) {

    @PostMapping("/intents")
    fun createPaymentIntent(@Valid @RequestBody request: PaymentIntentDto): ResponseEntity<ApiResponse<PaymentDto>> {
        val payment = paymentService.createPaymentIntent(request)
        return ResponseEntity.ok(ApiResponse.success(payment))
    }

    @GetMapping("/{id}")
    fun getPayment(@PathVariable id: UUID): ResponseEntity<ApiResponse<PaymentDto>> {
        val payment = paymentService.getPayment(id)
        return ResponseEntity.ok(ApiResponse.success(payment))
    }

    @PostMapping("/{id}/refund")
    fun refundPayment(
        @PathVariable id: UUID,
        @Valid @RequestBody request: RefundDto
    ): ResponseEntity<ApiResponse<PaymentDto>> {
        val payment = paymentService.refundPayment(id, request)
        return ResponseEntity.ok(ApiResponse.success(payment))
    }

    @GetMapping
    fun listPayments(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<ApiResponse<Page<PaymentDto>>> {
        val result = paymentService.listPayments(page, size)
        return ResponseEntity.ok(ApiResponse.success(result))
    }

    @PostMapping("/webhook/stripe")
    fun stripeWebhook(@RequestBody payload: String, @RequestHeader("Stripe-Signature") signature: String): ResponseEntity<ApiResponse<Void>> {
        paymentService.handleStripeWebhook(payload, signature)
        return ResponseEntity.ok(ApiResponse.success(null))
    }

    @PostMapping("/webhook/paypal")
    fun paypalWebhook(@RequestBody payload: String): ResponseEntity<ApiResponse<Void>> {
        paymentService.handlePaypalWebhook(payload)
        return ResponseEntity.ok(ApiResponse.success(null))
    }

    @GetMapping("/methods")
    fun getPaymentMethods(): ResponseEntity<ApiResponse<List<PaymentMethodDto>>> {
        val methods = paymentService.getPaymentMethods()
        return ResponseEntity.ok(ApiResponse.success(methods))
    }

    @PostMapping("/methods")
    fun addPaymentMethod(@Valid @RequestBody method: PaymentMethodDto): ResponseEntity<ApiResponse<PaymentMethodDto>> {
        val created = paymentService.addPaymentMethod(method)
        return ResponseEntity.ok(ApiResponse.success(created))
    }
}

data class Page<T>(val content: List<T>, val page: Int, val size: Int, val total: Long)
data class PaymentMethodDto(
    val id: UUID? = null,
    val type: String = "",
    val provider: String = "",
    val lastFour: String? = null,
    val expiryMonth: Int? = null,
    val expiryYear: Int? = null,
    val isDefault: Boolean = false
)
