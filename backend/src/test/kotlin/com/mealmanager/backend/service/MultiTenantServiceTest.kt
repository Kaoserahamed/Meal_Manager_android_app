package com.mealmanager.backend.service

import com.mealmanager.backend.model.dto.MembershipDto
import com.mealmanager.backend.model.dto.OrganizationDto
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MultiTenantServiceTest {

    private lateinit var service: MultiTenantService

    @BeforeEach
    fun setUp() { service = MultiTenantService() }

    @Test
    fun `createOrganization assigns id`() {
        val dto = OrganizationDto(name = "Acme", plan = "pro")
        val result = service.createOrganization(dto)
        assertNotNull(result.id)
    }

    @Test
    fun `getOrganization returns organization`() {
        val id = java.util.UUID.randomUUID()
        val result = service.getOrganization(id)
        assertEquals(id, result.id)
    }

    @Test
    fun `addMembership assigns id`() {
        val dto = MembershipDto(role = "admin")
        val result = service.addMembership(java.util.UUID.randomUUID(), dto)
        assertNotNull(result.id)
    }

    @Test
    fun `listOrganizations returns empty page`() {
        val page = service.listOrganizations(0, 20)
        assertEquals(0, page.total)
    }
}
