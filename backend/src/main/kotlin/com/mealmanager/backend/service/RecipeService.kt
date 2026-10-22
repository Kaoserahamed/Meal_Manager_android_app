package com.mealmanager.backend.service

import com.mealmanager.backend.model.dto.RecipeDto
import com.mealmanager.backend.model.dto.Page
import com.mealmanager.backend.model.dto.NutritionInfoDto
import com.mealmanager.backend.model.dto.ShareTokenDto
import com.mealmanager.backend.repository.RecipeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional
class RecipeService(
    private val recipeRepository: RecipeRepository
) {

    fun listRecipes(
        tag: String?,
        dietary: String?,
        page: Int,
        size: Int
    ): Page<RecipeDto> {
        val recipes = recipeRepository.findAll(tag, dietary, page, size)
        return Page(
            content = recipes.map { it.toDto() },
            page = page,
            size = size,
            total = recipeRepository.count(tag, dietary)
        )
    }

    fun getRecipeById(id: UUID): RecipeDto {
        return recipeRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Recipe not found: $id") }
            .toDto()
    }

    fun createRecipe(dto: RecipeDto): RecipeDto {
        val entity = dto.toEntity()
        val saved = recipeRepository.save(entity)
        return saved.toDto()
    }

    fun updateRecipe(id: UUID, dto: RecipeDto): RecipeDto {
        val entity = recipeRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Recipe not found: $id") }
        entity.updateFromDto(dto)
        val saved = recipeRepository.save(entity)
        return saved.toDto()
    }

    fun deleteRecipe(id: UUID) {
        if (!recipeRepository.existsById(id)) {
            throw IllegalArgumentException("Recipe not found: $id")
        }
        recipeRepository.deleteById(id)
    }

    fun getRecipeNutrition(id: UUID): NutritionInfoDto {
        val entity = recipeRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Recipe not found: $id") }
        return entity.calculateNutritionPerServing()
    }

    fun scaleRecipe(id: UUID, servings: Int): RecipeDto {
        val entity = recipeRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Recipe not found: $id") }
        val scaled = entity.scaleToServings(servings)
        val saved = recipeRepository.save(scaled)
        return saved.toDto()
    }

    fun toggleFavorite(id: UUID): Boolean {
        val entity = recipeRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Recipe not found: $id") }
        entity.isFavorite = !entity.isFavorite
        recipeRepository.save(entity)
        return entity.isFavorite
    }

    fun shareRecipe(id: UUID): ShareTokenDto {
        val entity = recipeRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Recipe not found: $id") }
        val token = ShareTokenDto(
            token = java.util.UUID.randomUUID().toString(),
            recipeId = id,
            expiresAt = java.time.Instant.now().plusSeconds(86400 * 7).toString()
        )
        recipeRepository.shareRecipe(id, token.token)
        return token
    }
}
