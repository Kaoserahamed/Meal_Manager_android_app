package com.mealmanager.backend.service

import com.mealmanager.backend.model.dto.Page
import com.mealmanager.backend.model.dto.AdminUserDto
import com.mealmanager.backend.model.dto.SystemHealthDto
import com.mealmanager.backend.model.dto.SystemStatsDto
import org.springframework.stereotype.Service

@Service
class AdminService {

    fun listUsers(page: Int, size: Int): Page<AdminUserDto> {
        return Page(content = emptyList(), page = page, size = size, total = 0)
    }

    fun getSystemHealth(): SystemHealthDto {
        return SystemHealthDto(status = "healthy", database = "connected", cache = "connected")
    }

    fun getSystemStats(): SystemStatsDto {
        return SystemStatsDto()
    }
}
