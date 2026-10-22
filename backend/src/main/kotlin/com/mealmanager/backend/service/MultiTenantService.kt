package com.mealmanager.backend.service

import com.mealmanager.backend.model.dto.Page
import com.mealmanager.backend.model.dto.OrganizationDto
import com.mealmanager.backend.model.dto.MembershipDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional
class MultiTenantService {

    fun createOrganization(dto: OrganizationDto): OrganizationDto {
        return dto.copy(id = UUID.randomUUID())
    }

    fun getOrganization(id: UUID): OrganizationDto {
        return OrganizationDto(id = id, name = "Default Org", plan = "free")
    }

    fun updateOrganization(id: UUID, dto: OrganizationDto): OrganizationDto {
        return dto.copy(id = id)
    }

    fun deleteOrganization(id: UUID) {}

    fun listOrganizations(page: Int, size: Int): Page<OrganizationDto> {
        return Page(content = emptyList(), page = page, size = size, total = 0)
    }

    fun addMembership(orgId: UUID, dto: MembershipDto): MembershipDto {
        return dto.copy(id = UUID.randomUUID())
    }

    fun listMemberships(orgId: UUID): List<MembershipDto> {
        return emptyList()
    }

    fun removeMembership(orgId: UUID, userId: UUID) {}
}
