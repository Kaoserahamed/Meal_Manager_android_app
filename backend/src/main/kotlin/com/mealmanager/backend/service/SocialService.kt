package com.mealmanager.backend.service

import com.mealmanager.backend.model.dto.ShareDto
import com.mealmanager.backend.model.dto.SharedPlanDto
import com.mealmanager.backend.model.dto.CollectionDto
import com.mealmanager.backend.model.dto.RecipeSummaryDto
import com.mealmanager.backend.model.dto.RatingDto
import com.mealmanager.backend.repository.SocialRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional
class SocialService(
    private val socialRepository: SocialRepository
) {

    fun shareMealPlan(request: com.mealmanager.backend.controller.ShareMealPlanRequest): ShareDto {
        val entity = request.toEntity()
        val saved = socialRepository.saveShare(entity)
        return saved.toDto()
    }

    fun getSharedPlan(shareId: UUID): SharedPlanDto {
        return socialRepository.findShareById(shareId)
            .orElseThrow { IllegalArgumentException("Share not found: $shareId") }
            .toDto()
    }

    fun createCollection(collection: CollectionDto): CollectionDto {
        val entity = collection.toEntity()
        val saved = socialRepository.saveCollection(entity)
        return saved.toDto()
    }

    fun listCollections(): List<CollectionDto> {
        return socialRepository.findAllCollections().map { it.toDto() }
    }

    fun getCollectionRecipes(id: UUID): List<RecipeSummaryDto> {
        return socialRepository.findCollectionRecipes(id).map { it.toSummaryDto() }
    }

    fun createRating(rating: RatingDto): RatingDto {
        val entity = rating.toEntity()
        val saved = socialRepository.saveRating(entity)
        return saved.toDto()
    }

    fun listRecipeRatings(recipeId: UUID): List<RatingDto> {
        return socialRepository.findRatingsByRecipe(recipeId).map { it.toDto() }
    }

    fun getAverageRating(recipeId: UUID): Double {
        val ratings = socialRepository.findRatingsByRecipe(recipeId)
        return if (ratings.isEmpty()) 0.0 else ratings.averageOf { it.rating }
    }
}
