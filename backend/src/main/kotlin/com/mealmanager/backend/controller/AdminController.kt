package com.mealmanager.backend.controller

import com.mealmanager.backend.model.dto.ApiResponse
import com.mealmanager.backend.model.dto.AdminUserDto
import com.mealmanager.backend.model.dto.SystemHealthDto
import com.mealmanager.backend.service.AdminService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/admin")
class AdminController(
    private val adminService: AdminService
) {

    @GetMapping("/users")
    fun listUsers(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<ApiResponse<Page<AdminUserDto>>> {
        val result = adminService.listUsers(page, size)
        return ResponseEntity.ok(ApiResponse.success(result))
    }

    @GetMapping("/health")
    fun getSystemHealth(): ResponseEntity<ApiResponse<SystemHealthDto>> {
        val health = adminService.getSystemHealth()
        return ResponseEntity.ok(ApiResponse.success(health))
    }

    @GetMapping("/stats")
    fun getSystemStats(): ResponseEntity<ApiResponse<SystemStatsDto>> {
        val stats = adminService.getSystemStats()
        return ResponseEntity.ok(ApiResponse.success(stats))
    }
}

data class SystemStatsDto(
    val totalUsers: Long = 0,
    val totalRecipes: Long = 0,
    val totalMealPlans: Long = 0,
    val totalOrders: Long = 0,
    val revenue: Double = 0.0
)
