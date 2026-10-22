package com.mealmanager.backend.controller

import com.mealmanager.backend.model.dto.ApiResponse
import com.mealmanager.backend.model.dto.AnalyticsReportDto
import com.mealmanager.backend.model.dto.MetricsSummaryDto
import com.mealmanager.backend.service.AnalyticsService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/api/v1/analytics")
class AnalyticsController(
    private val analyticsService: AnalyticsService
) {

    @GetMapping("/summary")
    fun getSummary(
        @RequestParam startDate: LocalDate,
        @RequestParam endDate: LocalDate
    ): ResponseEntity<ApiResponse<MetricsSummaryDto>> {
        val summary = analyticsService.getSummary(startDate, endDate)
        return ResponseEntity.ok(ApiResponse.success(summary))
    }

    @GetMapping("/reports")
    fun listReports(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<ApiResponse<Page<AnalyticsReportDto>>> {
        val result = analyticsService.listReports(page, size)
        return ResponseEntity.ok(ApiResponse.success(result))
    }

    @PostMapping("/reports")
    fun generateReport(@Valid @RequestBody request: GenerateReportRequest): ResponseEntity<ApiResponse<AnalyticsReportDto>> {
        val report = analyticsService.generateReport(request)
        return ResponseEntity.ok(ApiResponse.success(report))
    }

    @GetMapping("/reports/{id}")
    fun getReport(@PathVariable id: UUID): ResponseEntity<ApiResponse<AnalyticsReportDto>> {
        val report = analyticsService.getReport(id)
        return ResponseEntity.ok(ApiResponse.success(report))
    }

    @GetMapping("/trends/nutrition")
    fun getNutritionTrends(
        @RequestParam range: String
    ): ResponseEntity<ApiResponse<NutritionTrendDto>> {
        val trends = analyticsService.getNutritionTrends(range)
        return ResponseEntity.ok(ApiResponse.success(trends))
    }

    @GetMapping("/trends/engagement")
    fun getEngagementTrends(
        @RequestParam range: String
    ): ResponseEntity<ApiResponse<EngagementTrendDto>> {
        val trends = analyticsService.getEngagementTrends(range)
        return ResponseEntity.ok(ApiResponse.success(trends))
    }
}

import java.util.UUID

data class GenerateReportRequest(
    val reportType: String = "",
    val startDate: LocalDate = LocalDate.now(),
    val endDate: LocalDate = LocalDate.now(),
    val format: String = "pdf"
)
data class NutritionTrendDto(
    val range: String = "",
    val dailyCalories: List<Double> = emptyList(),
    val dates: List<String> = emptyList()
)
data class EngagementTrendDto(
    val range: String = "",
    val dailyActiveUsers: List<Int> = emptyList(),
    val dates: List<String> = emptyList()
)
