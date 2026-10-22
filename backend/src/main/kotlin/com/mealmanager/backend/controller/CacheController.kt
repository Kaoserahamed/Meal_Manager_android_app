package com.mealmanager.backend.controller

import com.mealmanager.backend.model.dto.ApiResponse
import com.mealmanager.backend.model.dto.CacheStatsDto
import com.mealmanager.backend.service.CacheService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/cache")
class CacheController(
    private val cacheService: CacheService
) {

    @GetMapping("/stats")
    fun getStats(): ResponseEntity<ApiResponse<CacheStatsDto>> {
        val stats = cacheService.getStats()
        return ResponseEntity.ok(ApiResponse.success(stats))
    }

    @DeleteMapping
    fun clearCache(@RequestParam(required = false) key: String): ResponseEntity<ApiResponse<Void>> {
        cacheService.clearCache(key)
        return ResponseEntity.ok(ApiResponse.success(null))
    }

    @GetMapping("/{key}")
    fun getCacheEntry(@RequestParam key: String): ResponseEntity<ApiResponse<String>> {
        val value = cacheService.get(key)
        return ResponseEntity.ok(ApiResponse.success(value))
    }
}
