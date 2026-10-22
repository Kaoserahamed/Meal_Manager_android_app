package com.mealmanager.backend.service

import com.mealmanager.backend.model.dto.AIFoodRecognitionResult
import com.mealmanager.backend.model.dto.MealPhotoDto
import com.mealmanager.backend.model.dto.SmartSuggestionDto
import com.mealmanager.backend.model.dto.RecipeSummaryDto
import com.mealmanager.backend.repository.RecipeRepository
import com.mealmanager.backend.repository.FridgeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@Service
@Transactional
class AIFoodRecognitionService(
    private val recipeRepository: RecipeRepository
) {

    fun recognizeFood(file: MultipartFile): AIFoodRecognitionResult {
        // Placeholder: would invoke a vision model in production
        return AIFoodRecognitionResult(
            foodName = "Recognized food",
            confidence = 0.92,
            estimatedPortionGrams = 200.0,
            nutrition = com.mealmanager.backend.model.dto.NutritionInfoDto(
                calories = 350.0,
                protein = 25.0,
                carbs = 30.0,
                fat = 15.0
            ),
            suggestedRecipeIds = recipeRepository.findAll(0, 10).content.map { it.id }
        )
    }

    fun logMealPhoto(file: MultipartFile, dto: MealPhotoDto): MealPhotoDto {
        val entity = dto.toEntity()
        val saved = recipeRepository.saveMealPhoto(entity)
        return saved.toDto()
    }
}

@Service
@Transactional
class RecipeRecommendationService(
    private val recipeRepository: RecipeRepository,
    private val fridgeRepository: FridgeRepository
) {

    fun getSmartSuggestions(): List<SmartSuggestionDto> {
        val fridgeItems = fridgeRepository.findAll()
        return fridgeItems.map { item ->
            SmartSuggestionDto(
                itemName = item.name,
                suggestedRecipeIds = recipeRepository.findByIngredient(item.name, 0, 5).content.map { it.id },
                reason = "Based on fridge ingredient: ${item.name}"
            )
        }
    }

    fun getRecipeRecommendations(): List<RecipeSummaryDto> {
        return recipeRepository.findAll(0, 20).content.map { it.toSummaryDto() }
    }
}

@Service
@Transactional
class MealPrepService(
    private val recipeRepository: RecipeRepository
) {

    fun getPrepSteps(): List<com.mealmanager.backend.controller.PrepStepDto> {
        return listOf(
            com.mealmanager.backend.controller.PrepStepDto(1, "Chop vegetables", 10),
            com.mealmanager.backend.controller.PrepStepDto(2, "Marinate protein", 15),
            com.mealmanager.backend.controller.PrepStepDto(3, "Preheat oven", 5)
        )
    }

    fun getMakeAheadMeals(): List<com.mealmanager.backend.controller.MakeAheadMealDto> {
        return listOf(
            com.mealmanager.backend.controller.MakeAheadMealDto(
                id = UUID.randomUUID(),
                name = "Chicken burrito bowls",
                storageInstructions = "Store in airtight container, refrigerate up to 4 days",
                expirationDate = java.time.LocalDate.now().plusDays(4).toString()
            )
        )
    }
}

@Service
@Transactional
class CookingModeService {
    fun createTimer(request: com.mealmanager.backend.controller.CookingTimerRequest): com.mealmanager.backend.controller.TimerDto {
        return com.mealmanager.backend.controller.TimerDto(
            timerId = UUID.randomUUID(),
            remainingSeconds = request.durationSeconds
        )
    }

    fun getCookingMode(recipeId: UUID): com.mealmanager.backend.controller.CookingModeDto {
        return com.mealmanager.backend.controller.CookingModeDto(
            recipeId = recipeId,
            currentStep = 0,
            handsFree = true
        )
    }
}

@Service
@Transactional
class VoiceService {
    fun logMealByVoice(request: com.mealmanager.backend.controller.VoiceMealLogRequest) {
        // Placeholder: parse transcript into a meal log
    }

    fun getVoiceShortcuts(): List<com.mealmanager.backend.controller.VoiceShortcutDto> {
        return listOf(
            com.mealmanager.backend.controller.VoiceShortcutDto("Log breakfast", "log_meal"),
            com.mealmanager.backend.controller.VoiceShortcutDto("Add to grocery list", "add_grocery")
        )
    }
}

@Service
@Transactional
class WearableService {
    fun syncWearableData(): com.mealmanager.backend.controller.WearableSyncDto {
        return com.mealmanager.backend.controller.WearableSyncDto(
            syncedAt = java.time.Instant.now().toString(),
            recordsSynced = 42
        )
    }

    fun getGoogleFitData(): com.mealmanager.backend.controller.GoogleFitDto {
        return com.mealmanager.backend.controller.GoogleFitDto(steps = 8500, calories = 320.0, activeMinutes = 45)
    }

    fun getHealthKitData(): com.mealmanager.backend.controller.HealthKitDto {
        return com.mealmanager.backend.controller.HealthKitDto(steps = 9100, calories = 340.0, workouts = 3)
    }
}
