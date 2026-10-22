package com.mealmanager.backend.service

import com.mealmanager.backend.model.dto.AdminUserDto
import com.mealmanager.backend.model.dto.SystemHealthDto
import com.mealmanager.backend.model.dto.SystemStatsDto
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AdminServiceTest {

    private lateinit var service: AdminService

    @BeforeEach
    fun setUp() { service = AdminService() }

    @Test
    fun `listUsers returns empty page`() {
        val page = service.listUsers(0, 20)
        assertEquals(0, page.total)
    }

    @Test
    fun `getSystemHealth returns healthy`() {
        val health = service.getSystemHealth()
        assertEquals("healthy", health.status)
    }

    @Test
    fun `getSystemStats returns stats`() {
        val stats = service.getSystemStats()
        assertNotNull(stats)
    }
}
