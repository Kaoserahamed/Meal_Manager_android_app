package com.mealmanager.backend.controller

import com.mealmanager.backend.model.dto.ApiResponse
import com.mealmanager.backend.model.dto.OrganizationDto
import com.mealmanager.backend.model.dto.MembershipDto
import com.mealmanager.backend.service.MultiTenantService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid
import java.util.UUID

@RestController
@RequestMapping("/api/v1/organizations")
class OrganizationController(
    private val multiTenantService: MultiTenantService
) {

    @PostMapping
    fun createOrganization(@Valid @RequestBody dto: OrganizationDto): ResponseEntity<ApiResponse<OrganizationDto>> {
        val created = multiTenantService.createOrganization(dto)
        return ResponseEntity.ok(ApiResponse.success(created))
    }

    @GetMapping("/{id}")
    fun getOrganization(@PathVariable id: UUID): ResponseEntity<ApiResponse<OrganizationDto>> {
        val org = multiTenantService.getOrganization(id)
        return ResponseEntity.ok(ApiResponse.success(org))
    }

    @PutMapping("/{id}")
    fun updateOrganization(
        @PathVariable id: UUID,
        @Valid @RequestBody dto: OrganizationDto
    ): ResponseEntity<ApiResponse<OrganizationDto>> {
        val updated = multiTenantService.updateOrganization(id, dto)
        return ResponseEntity.ok(ApiResponse.success(updated))
    }

    @DeleteMapping("/{id}")
    fun deleteOrganization(@PathVariable id: UUID): ResponseEntity<ApiResponse<Void>> {
        multiTenantService.deleteOrganization(id)
        return ResponseEntity.ok(ApiResponse.success(null))
    }

    @GetMapping
    fun listOrganizations(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<ApiResponse<Page<OrganizationDto>>> {
        val result = multiTenantService.listOrganizations(page, size)
        return ResponseEntity.ok(ApiResponse.success(result))
    }

    @PostMapping("/{orgId}/memberships")
    fun addMembership(
        @PathVariable orgId: UUID,
        @Valid @RequestBody dto: MembershipDto
    ): ResponseEntity<ApiResponse<MembershipDto>> {
        val created = multiTenantService.addMembership(orgId, dto)
        return ResponseEntity.ok(ApiResponse.success(created))
    }

    @GetMapping("/{orgId}/memberships")
    fun listMemberships(@PathVariable orgId: UUID): ResponseEntity<ApiResponse<List<MembershipDto>>> {
        val memberships = multiTenantService.listMemberships(orgId)
        return ResponseEntity.ok(ApiResponse.success(memberships))
    }

    @DeleteMapping("/{orgId}/memberships/{userId}")
    fun removeMembership(
        @PathVariable orgId: UUID,
        @PathVariable userId: UUID
    ): ResponseEntity<ApiResponse<Void>> {
        multiTenantService.removeMembership(orgId, userId)
        return ResponseEntity.ok(ApiResponse.success(null))
    }
}
