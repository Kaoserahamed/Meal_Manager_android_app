package com.mealmanager.backend.service

import com.mealmanager.backend.model.dto.RateLimitDto
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RateLimitServiceTest {

    private lateinit var service: RateLimitService

    @BeforeEach
    fun setUp() { service = RateLimitService() }

    @Test
    fun `getStatus returns rate limit info`() {
        val status = service.getStatus()
        assertTrue(status.remaining >= 0)
    }

    @Test
    fun `getConfig returns config`() {
        val config = service.getConfig()
        assertTrue(config.enabled)
    }
}
