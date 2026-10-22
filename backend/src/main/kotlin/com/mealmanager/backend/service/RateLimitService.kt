package com.mealmanager.backend.service

import com.mealmanager.backend.model.dto.RateLimitDto
import org.springframework.stereotype.Service

@Service
class RateLimitService {

    fun getStatus(): RateLimitDto {
        return RateLimitDto(remaining = 59, resetAt = java.time.Instant.now().plusSeconds(60).toString())
    }

    fun getConfig(): com.mealmanager.backend.controller.RateLimitConfigDto {
        return com.mealmanager.backend.controller.RateLimitConfigDto()
    }
}
