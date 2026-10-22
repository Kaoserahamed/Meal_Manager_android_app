package com.mealmanager.backend.controller

import com.mealmanager.backend.model.dto.FridgeItemDto
import com.mealmanager.backend.model.dto.PantryItemDto
import com.mealmanager.backend.model.dto.ApiResponse
import com.mealmanager.backend.service.FridgeService
import com.mealmanager.backend.service.PantryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid
import java.util.UUID

@RestController
@RequestMapping("/api/v1/fridge")
class FridgeController(
    private val fridgeService: FridgeService
) {

    @GetMapping
    fun listFridgeItems(): ResponseEntity<ApiResponse<List<FridgeItemDto>>> {
        val items = fridgeService.listFridgeItems()
        return ResponseEntity.ok(ApiResponse.success(items))
    }

    @PostMapping
    fun addFridgeItem(@Valid @RequestBody item: FridgeItemDto): ResponseEntity<ApiResponse<FridgeItemDto>> {
        val created = fridgeService.addFridgeItem(item)
        return ResponseEntity.ok(ApiResponse.success(created))
    }

    @PutMapping("/{id}")
    fun updateFridgeItem(
        @PathVariable id: UUID,
        @Valid @RequestBody item: FridgeItemDto
    ): ResponseEntity<ApiResponse<FridgeItemDto>> {
        val updated = fridgeService.updateFridgeItem(id, item)
        return ResponseEntity.ok(ApiResponse.success(updated))
    }

    @DeleteMapping("/{id}")
    fun deleteFridgeItem(@PathVariable id: UUID): ResponseEntity<ApiResponse<Void>> {
        fridgeService.deleteFridgeItem(id)
        return ResponseEntity.ok(ApiResponse.success(null))
    }

    @GetMapping("/expiring")
    fun getExpiringItems(
        @RequestParam(days) days: Int
    ): ResponseEntity<ApiResponse<List<FridgeItemDto>>> {
        val items = fridgeService.getExpiringItems(days)
        return ResponseEntity.ok(ApiResponse.success(items))
    }

    @GetMapping("/low-stock")
    fun getLowStockItems(): ResponseEntity<ApiResponse<List<FridgeItemDto>>> {
        val items = fridgeService.getLowStockItems()
        return ResponseEntity.ok(ApiResponse.success(items))
    }
}

@RestController
@RequestMapping("/api/v1/pantry")
class PantryController(
    private val pantryService: PantryService
) {

    @GetMapping
    fun listPantryItems(): ResponseEntity<ApiResponse<List<PantryItemDto>>> {
        val items = pantryService.listPantryItems()
        return ResponseEntity.ok(ApiResponse.success(items))
    }

    @PostMapping
    fun addPantryItem(@Valid @RequestBody item: PantryItemDto): ResponseEntity<ApiResponse<PantryItemDto>> {
        val created = pantryService.addPantryItem(item)
        return ResponseEntity.ok(ApiResponse.success(created))
    }

    @PutMapping("/{id}")
    fun updatePantryItem(
        @PathVariable id: UUID,
        @Valid @RequestBody item: PantryItemDto
    ): ResponseEntity<ApiResponse<PantryItemDto>> {
        val updated = pantryService.updatePantryItem(id, item)
        return ResponseEntity.ok(ApiResponse.success(updated))
    }

    @DeleteMapping("/{id}")
    fun deletePantryItem(@PathVariable id: UUID): ResponseEntity<ApiResponse<Void>> {
        pantryService.deletePantryItem(id)
        return ResponseEntity.ok(ApiResponse.success(null))
    }

    @GetMapping("/reorder-thresholds")
    fun getReorderThresholds(): ResponseEntity<ApiResponse<List<ReorderThresholdDto>>> {
        val thresholds = pantryService.getReorderThresholds()
        return ResponseEntity.ok(ApiResponse.success(thresholds))
    }

    @PostMapping("/auto-add-to-grocery")
    fun autoAddToGrocery(): ResponseEntity<ApiResponse<GrocerySuggestionDto>> {
        val suggestion = pantryService.autoAddToGrocery()
        return ResponseEntity.ok(ApiResponse.success(suggestion))
    }
}

data class ReorderThresholdDto(
    val itemId: UUID,
    val itemName: String,
    val threshold: Int,
    val currentQuantity: Int
)

data class GrocerySuggestionDto(
    val items: List<String>,
    val totalEstimatedCost: Double
)
