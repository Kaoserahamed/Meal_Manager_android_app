package com.mealmanager.backend.controller

import com.mealmanager.backend.model.dto.RecipeDto
import com.mealmanager.backend.model.dto.ApiResponse
import com.mealmanager.backend.service.RecipeService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid
import java.util.UUID

@RestController
@RequestMapping("/api/v1/recipes")
class RecipeController(
    private val recipeService: RecipeService
) {

    @GetMapping
    fun listRecipes(
        @RequestParam(required = false) tag: String?,
        @RequestParam(required = false) dietary: String?,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<ApiResponse<Page<RecipeDto>>> {
        val result = recipeService.listRecipes(tag, dietary, page, size)
        return ResponseEntity.ok(ApiResponse.success(result))
    }

    @GetMapping("/{id}")
    fun getRecipe(@PathVariable id: UUID): ResponseEntity<ApiResponse<RecipeDto>> {
        val recipe = recipeService.getRecipeById(id)
        return ResponseEntity.ok(ApiResponse.success(recipe))
    }

    @PostMapping
    fun createRecipe(@Valid @RequestBody recipe: RecipeDto): ResponseEntity<ApiResponse<RecipeDto>> {
        val created = recipeService.createRecipe(recipe)
        return ResponseEntity.ok(ApiResponse.success(created))
    }

    @PutMapping("/{id}")
    fun updateRecipe(
        @PathVariable id: UUID,
        @Valid @RequestBody recipe: RecipeDto
    ): ResponseEntity<ApiResponse<RecipeDto>> {
        val updated = recipeService.updateRecipe(id, recipe)
        return ResponseEntity.ok(ApiResponse.success(updated))
    }

    @DeleteMapping("/{id}")
    fun deleteRecipe(@PathVariable id: UUID): ResponseEntity<ApiResponse<Void>> {
        recipeService.deleteRecipe(id)
        return ResponseEntity.ok(ApiResponse.success(null))
    }

    @GetMapping("/{id}/nutrition")
    fun getRecipeNutrition(@PathVariable id: UUID): ResponseEntity<ApiResponse<NutritionInfoDto>> {
        val nutrition = recipeService.getRecipeNutrition(id)
        return ResponseEntity.ok(ApiResponse.success(nutrition))
    }

    @PostMapping("/{id}/scale")
    fun scaleRecipe(
        @PathVariable id: UUID,
        @RequestParam servings: Int
    ): ResponseEntity<ApiResponse<RecipeDto>> {
        val scaled = recipeService.scaleRecipe(id, servings)
        return ResponseEntity.ok(ApiResponse.success(scaled))
    }

    @PostMapping("/{id}/favorite")
    fun toggleFavorite(@PathVariable id: UUID): ResponseEntity<ApiResponse<Boolean>> {
        val favored = recipeService.toggleFavorite(id)
        return ResponseEntity.ok(ApiResponse.success(favored))
    }

    @PostMapping("/{id}/share")
    fun shareRecipe(@PathVariable id: UUID): ResponseEntity<ApiResponse<ShareTokenDto>> {
        val token = recipeService.shareRecipe(id)
        return ResponseEntity.ok(ApiResponse.success(token))
    }
}
