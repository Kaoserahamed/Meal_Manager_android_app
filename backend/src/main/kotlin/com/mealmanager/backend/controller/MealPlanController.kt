package com.mealmanager.backend.controller

import com.mealmanager.backend.model.dto.MealPlanDto
import com.mealmanager.backend.model.dto.ApiResponse
import com.mealmanager.backend.service.MealPlanService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid
import java.util.UUID

@RestController
@RequestMapping("/api/v1/meal-plans")
class MealPlanController(
    private val mealPlanService: MealPlanService
) {

    @GetMapping
    fun listMealPlans(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<ApiResponse<Page<MealPlanDto>>> {
        val result = mealPlanService.listMealPlans(page, size)
        return ResponseEntity.ok(ApiResponse.success(result))
    }

    @GetMapping("/{id}")
    fun getMealPlan(@PathVariable id: UUID): ResponseEntity<ApiResponse<MealPlanDto>> {
        val plan = mealPlanService.getMealPlanById(id)
        return ResponseEntity.ok(ApiResponse.success(plan))
    }

    @PostMapping
    fun createMealPlan(@Valid @RequestBody plan: MealPlanDto): ResponseEntity<ApiResponse<MealPlanDto>> {
        val created = mealPlanService.createMealPlan(plan)
        return ResponseEntity.ok(ApiResponse.success(created))
    }

    @PutMapping("/{id}")
    fun updateMealPlan(
        @PathVariable id: UUID,
        @Valid @RequestBody plan: MealPlanDto
    ): ResponseEntity<ApiResponse<MealPlanDto>> {
        val updated = mealPlanService.updateMealPlan(id, plan)
        return ResponseEntity.ok(ApiResponse.success(updated))
    }

    @DeleteMapping("/{id}")
    fun deleteMealPlan(@PathVariable id: UUID): ResponseEntity<ApiResponse<Void>> {
        mealPlanService.deleteMealPlan(id)
        return ResponseEntity.ok(ApiResponse.success(null))
    }

    @PutMapping("/{id}/meals")
    fun updateMeals(
        @PathVariable id: UUID,
        @Valid @RequestBody meals: List<MealAssignmentDto>
    ): ResponseEntity<ApiResponse<MealPlanDto>> {
        val updated = mealPlanService.updateMeals(id, meals)
        return ResponseEntity.ok(ApiResponse.success(updated))
    }

    @GetMapping("/{id}/grocery-list")
    fun getGroceryList(@PathVariable id: UUID): ResponseEntity<ApiResponse<GroceryListDto>> {
        val list = mealPlanService.getGroceryList(id)
        return ResponseEntity.ok(ApiResponse.success(list))
    }

    @PostMapping("/{id}/templates")
    fun createTemplate(
        @PathVariable id: UUID,
        @Valid @RequestBody template: MealPlanTemplateDto
    ): ResponseEntity<ApiResponse<MealPlanTemplateDto>> {
        val created = mealPlanService.createTemplate(id, template)
        return ResponseEntity.ok(ApiResponse.success(created))
    }

    @GetMapping("/templates")
    fun listTemplates(): ResponseEntity<ApiResponse<List<MealPlanTemplateDto>>> {
        val templates = mealPlanService.listTemplates()
        return ResponseEntity.ok(ApiResponse.success(templates))
    }
}
