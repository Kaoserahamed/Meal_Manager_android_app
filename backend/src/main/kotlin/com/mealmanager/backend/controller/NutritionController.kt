package com.mealmanager.backend.controller

import com.mealmanager.backend.model.dto.NutritionLogDto
import com.mealmanager.backend.model.dto.NutritionGoalDto
import com.mealmanager.backend.model.dto.ApiResponse
import com.mealmanager.backend.service.NutritionService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid
import java.time.LocalDate
import java.util.UUID

@RestController
@RequestMapping("/api/v1/nutrition")
class NutritionController(
    private val nutritionService: NutritionService
) {

    @GetMapping("/logs")
    fun listNutritionLogs(
        @RequestParam startDate: LocalDate,
        @RequestParam endDate: LocalDate
    ): ResponseEntity<ApiResponse<List<NutritionLogDto>>> {
        val logs = nutritionService.listNutritionLogs(startDate, endDate)
        return ResponseEntity.ok(ApiResponse.success(logs))
    }

    @PostMapping("/logs")
    fun createNutritionLog(@Valid @RequestBody log: NutritionLogDto): ResponseEntity<ApiResponse<NutritionLogDto>> {
        val created = nutritionService.createNutritionLog(log)
        return ResponseEntity.ok(ApiResponse.success(created))
    }

    @GetMapping("/goals")
    fun getNutritionGoals(): ResponseEntity<ApiResponse<NutritionGoalDto>> {
        val goals = nutritionService.getNutritionGoals()
        return ResponseEntity.ok(ApiResponse.success(goals))
    }

    @PutMapping("/goals")
    fun updateNutritionGoals(@Valid @RequestBody goals: NutritionGoalDto): ResponseEntity<ApiResponse<NutritionGoalDto>> {
        val updated = nutritionService.updateNutritionGoals(goals)
        return ResponseEntity.ok(ApiResponse.success(updated))
    }

    @GetMapping("/daily-totals/{date}")
    fun getDailyTotals(@PathVariable date: LocalDate): ResponseEntity<ApiResponse<DailyTotalsDto>> {
        val totals = nutritionService.getDailyTotals(date)
        return ResponseEntity.ok(ApiResponse.success(totals))
    }

    @GetMapping("/progress")
    fun getProgress(): ResponseEntity<ApiResponse<NutritionProgressDto>> {
        val progress = nutritionService.getProgress()
        return ResponseEntity.ok(ApiResponse.success(progress))
    }

    @GetMapping("/trends")
    fun getNutritionTrends(
        @RequestParam(range) range: String
    ): ResponseEntity<ApiResponse<NutritionTrendsDto>> {
        val trends = nutritionService.getNutritionTrends(range)
        return ResponseEntity.ok(ApiResponse.success(trends))
    }
}
