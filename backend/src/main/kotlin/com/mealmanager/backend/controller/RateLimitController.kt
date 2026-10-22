package com.mealmanager.backend.controller

import com.mealmanager.backend.model.dto.ApiResponse
import com.mealmanager.backend.model.dto.RateLimitDto
import com.mealmanager.backend.service.RateLimitService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/rate-limit")
class RateLimitController(
    private val rateLimitService: RateLimitService
) {

    @GetMapping("/status")
    fun getStatus(): ResponseEntity<ApiResponse<RateLimitDto>> {
        val status = rateLimitService.getStatus()
        return ResponseEntity.ok(ApiResponse.success(status))
    }

    @GetMapping("/config")
    fun getConfig(): ResponseEntity<ApiResponse<RateLimitConfigDto>> {
        val config = rateLimitService.getConfig()
        return ResponseEntity.ok(ApiResponse.success(config))
    }
}

data class RateLimitConfigDto(
    val requestsPerMinute: Int = 60,
    val burstSize: Int = 10,
    val enabled: Boolean = true
)
