package com.mealmanager.backend.controller

import com.mealmanager.backend.model.dto.GroceryListDto
import com.mealmanager.backend.model.dto.GroceryItemDto
import com.mealmanager.backend.model.dto.ApiResponse
import com.mealmanager.backend.service.GroceryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid
import java.util.UUID

@RestController
@RequestMapping("/api/v1/grocery-lists")
class GroceryController(
    private val groceryService: GroceryService
) {

    @GetMapping
    fun listGroceryLists(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<ApiResponse<Page<GroceryListDto>>> {
        val result = groceryService.listGroceryLists(page, size)
        return ResponseEntity.ok(ApiResponse.success(result))
    }

    @GetMapping("/{id}")
    fun getGroceryList(@PathVariable id: UUID): ResponseEntity<ApiResponse<GroceryListDto>> {
        val list = groceryService.getGroceryListById(id)
        return ResponseEntity.ok(ApiResponse.success(list))
    }

    @PostMapping("/generate")
    fun generateGroceryList(
        @Valid @RequestBody request: GenerateGroceryListRequest
    ): ResponseEntity<ApiResponse<GroceryListDto>> {
        val list = groceryService.generateGroceryList(request.mealPlanId)
        return ResponseEntity.ok(ApiResponse.success(list))
    }

    @PatchMapping("/{listId}/items/{itemId}")
    fun toggleItemChecked(
        @PathVariable listId: UUID,
        @PathVariable itemId: UUID,
        @Valid @RequestBody request: ToggleItemRequest
    ): ResponseEntity<ApiResponse<Void>> {
        groceryService.toggleItemChecked(listId, itemId, request.checked)
        return ResponseEntity.ok(ApiResponse.success(null))
    }

    @DeleteMapping("/{id}")
    fun deleteGroceryList(@PathVariable id: UUID): ResponseEntity<ApiResponse<Void>> {
        groceryService.deleteGroceryList(id)
        return ResponseEntity.ok(ApiResponse.success(null))
    }

    @GetMapping("/categories")
    fun getCategories(): ResponseEntity<ApiResponse<List<String>>> {
        val categories = groceryService.getCategories()
        return ResponseEntity.ok(ApiResponse.success(categories))
    }

    @GetMapping("/aisles")
    fun getAisles(): ResponseEntity<ApiResponse<List<String>>> {
        val aisles = groceryService.getAisles()
        return ResponseEntity.ok(ApiResponse.success(aisles))
    }
}

data class GenerateGroceryListRequest(val mealPlanId: UUID)
data class ToggleItemRequest(val checked: Boolean)
