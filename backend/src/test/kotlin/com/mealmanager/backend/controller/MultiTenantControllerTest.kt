package com.mealmanager.backend.controller

import com.mealmanager.backend.service.MultiTenantService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MultiTenantControllerTest {

    private lateinit var controller: MultiTenantController

    @BeforeEach
    fun setUp() { controller = MultiTenantController(MultiTenantService()) }

    @Test
    fun `createOrganization returns 200`() {
        val response = controller.createOrganization(com.mealmanager.backend.model.dto.OrganizationDto(name = "Test"))
        assertEquals(200, response.statusCode.value())
    }

    @Test
    fun `listOrganizations returns 200`() {
        val response = controller.listOrganizations(0, 20)
        assertEquals(200, response.statusCode.value())
    }
}
