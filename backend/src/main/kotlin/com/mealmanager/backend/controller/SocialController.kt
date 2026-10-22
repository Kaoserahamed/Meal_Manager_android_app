package com.mealmanager.backend.controller

import com.mealmanager.backend.model.dto.ShareDto
import com.mealmanager.backend.model.dto.CollectionDto
import com.mealmanager.backend.model.dto.RatingDto
import com.mealmanager.backend.model.dto.ApiResponse
import com.mealmanager.backend.service.SocialService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid
import java.util.UUID

@RestController
@RequestMapping("/api/v1/social")
class SocialController(
    private val socialService: SocialService
) {

    @PostMapping("/sharing")
    fun shareMealPlan(@Valid @RequestBody request: ShareMealPlanRequest): ResponseEntity<ApiResponse<ShareDto>> {
        val share = socialService.shareMealPlan(request)
        return ResponseEntity.ok(ApiResponse.success(share))
    }

    @GetMapping("/sharing/{shareId}")
    fun getSharedPlan(@PathVariable shareId: UUID): ResponseEntity<ApiResponse<SharedPlanDto>> {
        val plan = socialService.getSharedPlan(shareId)
        return ResponseEntity.ok(ApiResponse.success(plan))
    }

    @PostMapping("/collections")
    fun createCollection(@Valid @RequestBody collection: CollectionDto): ResponseEntity<ApiResponse<CollectionDto>> {
        val created = socialService.createCollection(collection)
        return ResponseEntity.ok(ApiResponse.success(created))
    }

    @GetMapping("/collections")
    fun listCollections(): ResponseEntity<ApiResponse<List<CollectionDto>>> {
        val collections = socialService.listCollections()
        return ResponseEntity.ok(ApiResponse.success(collections))
    }

    @GetMapping("/collections/{id}/recipes")
    fun getCollectionRecipes(@PathVariable id: UUID): ResponseEntity<ApiResponse<List<RecipeSummaryDto>>> {
        val recipes = socialService.getCollectionRecipes(id)
        return ResponseEntity.ok(ApiResponse.success(recipes))
    }

    @PostMapping("/ratings")
    fun createRating(@Valid @RequestBody rating: RatingDto): ResponseEntity<ApiResponse<RatingDto>> {
        val created = socialService.createRating(rating)
        return ResponseEntity.ok(ApiResponse.success(created))
    }

    @GetMapping("/recipes/{recipeId}/ratings")
    fun listRecipeRatings(@PathVariable recipeId: UUID): ResponseEntity<ApiResponse<List<RatingDto>>> {
        val ratings = socialService.listRecipeRatings(recipeId)
        return ResponseEntity.ok(ApiResponse.success(ratings))
    }

    @GetMapping("/recipes/{recipeId}/average-rating")
    fun getAverageRating(@PathVariable recipeId: UUID): ResponseEntity<ApiResponse<Double>> {
        val avg = socialService.getAverageRating(recipeId)
        return ResponseEntity.ok(ApiResponse.success(avg))
    }
}

data class ShareMealPlanRequest(
    val planId: UUID,
    val sharedWith: List<String>,
    val permissions: List<String>
)

data class SharedPlanDto(
    val shareId: UUID,
    val planId: UUID,
    val sharedWith: List<String>,
    val permissions: List<String>
)

data class RecipeSummaryDto(
    val id: UUID,
    val title: String,
    val thumbnailUrl: String?
)
