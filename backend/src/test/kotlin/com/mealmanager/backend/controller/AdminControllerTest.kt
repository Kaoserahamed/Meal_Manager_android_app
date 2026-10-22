package com.mealmanager.backend.controller

import com.mealmanager.backend.service.AdminService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AdminControllerTest {

    private lateinit var controller: AdminController

    @BeforeEach
    fun setUp() { controller = AdminController(AdminService()) }

    @Test
    fun `listUsers returns 200`() {
        val response = controller.listUsers(0, 20)
        assertEquals(200, response.statusCode.value())
    }

    @Test
    fun `getSystemHealth returns 200`() {
        val response = controller.getSystemHealth()
        assertEquals(200, response.statusCode.value())
    }
}
