package com.mealmanager.backend.controller

import com.mealmanager.backend.model.dto.BarcodeScanResult
import com.mealmanager.backend.model.dto.PackagedFoodDto
import com.mealmanager.backend.model.dto.ApiResponse
import com.mealmanager.backend.service.BarcodeService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid
import java.util.UUID

@RestController
@RequestMapping("/api/v1/barcode")
class BarcodeController(
    private val barcodeService: BarcodeService
) {

    @PostMapping("/scan")
    fun scanBarcode(@Valid @RequestBody request: BarcodeScanRequest): ResponseEntity<ApiResponse<BarcodeScanResult>> {
        val result = barcodeService.scanBarcode(request.barcode)
        return ResponseEntity.ok(ApiResponse.success(result))
    }

    @GetMapping("/food/{barcode}")
    fun getPackagedFood(@PathVariable barcode: String): ResponseEntity<ApiResponse<PackagedFoodDto>> {
        val food = barcodeService.getPackagedFood(barcode)
        return ResponseEntity.ok(ApiResponse.success(food))
    }

    @PostMapping("/food")
    fun addPackagedFood(@Valid @RequestBody food: PackagedFoodDto): ResponseEntity<ApiResponse<PackagedFoodDto>> {
        val created = barcodeService.addPackagedFood(food)
        return ResponseEntity.ok(ApiResponse.success(created))
    }

    @GetMapping("/food/search")
    fun searchPackagedFoods(
        @RequestParam query: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<ApiResponse<Page<PackagedFoodDto>>> {
        val result = barcodeService.searchPackagedFoods(query, page, size)
        return ResponseEntity.ok(ApiResponse.success(result))
    }

    @PostMapping("/log-nutrition")
    fun logBarcodeNutrition(@Valid @RequestBody request: LogBarcodeNutritionRequest): ResponseEntity<ApiResponse<Void>> {
        barcodeService.logBarcodeNutrition(request)
        return ResponseEntity.ok(ApiResponse.success(null))
    }
}

data class BarcodeScanRequest(val barcode: String)
data class LogBarcodeNutritionRequest(
    val barcode: String,
    val userId: UUID,
    val date: String,
    val servings: Double
)
