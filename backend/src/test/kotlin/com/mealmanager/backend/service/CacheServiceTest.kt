package com.mealmanager.backend.service

import com.mealmanager.backend.model.dto.CacheStatsDto
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CacheServiceTest {

    private lateinit var service: CacheService

    @BeforeEach
    fun setUp() { service = CacheService() }

    @Test
    fun `getStats returns stats`() {
        val stats = service.getStats()
        assertTrue(stats.hitRate > 0.0)
        assertTrue(stats.totalEntries > 0)
    }

    @Test
    fun `clearCache does not throw`() {
        service.clearCache("key")
        service.clearCache(null)
    }

    @Test
    fun `get returns null for missing key`() {
        assertNull(service.get("missing"))
    }
}
