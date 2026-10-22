package com.mealmanager.backend.service

import com.mealmanager.backend.model.dto.LoyaltyAccountDto
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

class LoyaltyServiceTest {

    private lateinit var service: LoyaltyService

    @BeforeEach
    fun setUp() { service = LoyaltyService() }

    @Test
    fun `getAccount returns account`() {
        val userId = UUID.randomUUID()
        val account = service.getAccount(userId)
        assertEquals(userId, account.userId)
    }

    @Test
    fun `addPoints returns account with points`() {
        val req = com.mealmanager.backend.controller.AddPointsRequest(userId = UUID.randomUUID(), points = 100, reason = "test")
        val account = service.addPoints(req)
        assertEquals(100, account.points)
    }

    @Test
    fun `listRewards returns empty list`() {
        assertTrue(service.listRewards().isEmpty())
    }
}
