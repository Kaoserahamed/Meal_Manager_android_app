package com.mealmanager.backend.service

import com.mealmanager.backend.model.dto.CacheStatsDto
import org.springframework.stereotype.Service

@Service
class CacheService {

    fun getStats(): CacheStatsDto {
        return CacheStatsDto(hitRate = 0.95, totalEntries = 10000, memoryUsedMB = 256.0)
    }

    fun clearCache(key: String?) {}

    fun get(key: String): String? {
        return null
    }
}
