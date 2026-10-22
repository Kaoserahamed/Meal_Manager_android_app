package com.mealmanager.backend.service

import com.mealmanager.backend.model.dto.BarcodeScanResult
import com.mealmanager.backend.model.dto.PackagedFoodDto
import com.mealmanager.backend.repository.BarcodeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate

@Service
@Transactional
class BarcodeService(
    private val barcodeRepository: BarcodeRepository,
    private val restTemplate: RestTemplate
) {

    fun scanBarcode(barcode: String): BarcodeScanResult {
        val food = barcodeRepository.findByBarcode(barcode)
        return if (food != null) {
            BarcodeScanResult(
                barcode = barcode,
                found = true,
                foodName = food.name,
                brand = food.brand,
                nutrition = food.nutrition,
                ingredients = food.ingredients
            )
        } else {
            BarcodeScanResult(
                barcode = barcode,
                found = false,
                foodName = null,
                brand = null,
                nutrition = null,
                ingredients = null
            )
        }
    }

    fun getPackagedFood(barcode: String): PackagedFoodDto {
        return barcodeRepository.findByBarcode(barcode)
            .map { it.toDto() }
            .orElseThrow { IllegalArgumentException("Packaged food not found: $barcode") }
    }

    fun addPackagedFood(food: PackagedFoodDto): PackagedFoodDto {
        val entity = food.toEntity()
        val saved = barcodeRepository.save(entity)
        return saved.toDto()
    }

    fun searchPackagedFoods(query: String, page: Int, size: Int): com.mealmanager.backend.model.dto.Page<PackagedFoodDto> {
        val foods = barcodeRepository.search(query, page, size)
        return com.mealmanager.backend.model.dto.Page(
            content = foods.map { it.toDto() },
            page = page,
            size = size,
            total = barcodeRepository.count(query)
        )
    }

    fun logBarcodeNutrition(request: com.mealmanager.backend.controller.LogBarcodeNutritionRequest) {
        val food = barcodeRepository.findByBarcode(request.barcode)
            .orElseThrow { IllegalArgumentException("Packaged food not found: ${request.barcode}") }
        val nutritionLog = com.mealmanager.backend.model.entity.NutritionLogEntity(
            id = java.util.UUID.randomUUID(),
            userId = request.userId,
            date = request.date,
            meals = emptyList(),
            totals = food.nutrition.toDailyTotals(request.servings)
        )
        barcodeRepository.saveNutritionLog(nutritionLog)
    }
}
