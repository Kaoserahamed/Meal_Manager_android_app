package com.mealmanager.backend.controller

import com.mealmanager.backend.service.LoyaltyService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

class LoyaltyControllerTest {

    private lateinit var controller: LoyaltyController

    @BeforeEach
    fun setUp() { controller = LoyaltyController(LoyaltyService()) }

    @Test
    fun `getAccount returns 200`() {
        val response = controller.getAccount(UUID.randomUUID())
        assertEquals(200, response.statusCode.value())
    }

    @Test
    fun `listRewards returns 200`() {
        val response = controller.listRewards()
        assertEquals(200, response.statusCode.value())
    }
}
