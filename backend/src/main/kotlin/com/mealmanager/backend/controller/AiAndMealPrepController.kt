package com.mealmanager.backend.controller

import com.mealmanager.backend.model.dto.MealPhotoDto
import com.mealmanager.backend.model.dto.AIFoodRecognitionResult
import com.mealmanager.backend.model.dto.SmartSuggestionDto
import com.mealmanager.backend.model.dto.ApiResponse
import com.mealmanager.backend.service.AIFoodRecognitionService
import com.mealmanager.backend.service.RecipeRecommendationService
import com.mealmanager.backend.service.MealPrepService
import com.mealmanager.backend.service.CookingModeService
import com.mealmanager.backend.service.VoiceService
import com.mealmanager.backend.service.WearableService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import jakarta.validation.Valid
import java.util.UUID

@RestController
@RequestMapping("/api/v1/ai")
class AiController(
    private val recognitionService: AIFoodRecognitionService,
    private val recommendationService: RecipeRecommendationService
) {

    @PostMapping("/food-recognition")
    fun recognizeFood(
        @RequestPart file: MultipartFile
    ): ResponseEntity<ApiResponse<AIFoodRecognitionResult>> {
        val result = recognitionService.recognizeFood(file)
        return ResponseEntity.ok(ApiResponse.success(result))
    }

    @PostMapping("/meal-photo-log")
    fun logMealPhoto(
        @RequestPart file: MultipartFile,
        @RequestPart("dto") @Valid dto: MealPhotoDto
    ): ResponseEntity<ApiResponse<MealPhotoDto>> {
        val created = recognitionService.logMealPhoto(file, dto)
        return ResponseEntity.ok(ApiResponse.success(created))
    }

    @GetMapping("/smart-suggestions")
    fun getSmartSuggestions(): ResponseEntity<ApiResponse<List<SmartSuggestionDto>>> {
        val suggestions = recommendationService.getSmartSuggestions()
        return ResponseEntity.ok(ApiResponse.success(suggestions))
    }

    @GetMapping("/recipe-recommendations")
    fun getRecipeRecommendations(): ResponseEntity<ApiResponse<List<RecipeSummaryDto>>> {
        val recipes = recommendationService.getRecipeRecommendations()
        return ResponseEntity.ok(ApiResponse.success(recipes))
    }
}

@RestController
@RequestMapping("/api/v1/meal-prep")
class MealPrepController(
    private val prepService: MealPrepService,
    private val cookingModeService: CookingModeService
) {

    @GetMapping("/prep-steps")
    fun getPrepSteps(): ResponseEntity<ApiResponse<List<PrepStepDto>>> {
        val steps = prepService.getPrepSteps()
        return ResponseEntity.ok(ApiResponse.success(steps))
    }

    @GetMapping("/make-ahead")
    fun getMakeAheadMeals(): ResponseEntity<ApiResponse<List<MakeAheadMealDto>>> {
        val meals = prepService.getMakeAheadMeals()
        return ResponseEntity.ok(ApiResponse.success(meals))
    }

    @PostMapping("/timers")
    fun createCookingTimer(@Valid @RequestBody request: CookingTimerRequest): ResponseEntity<ApiResponse<TimerDto>> {
        val timer = cookingModeService.createTimer(request)
        return ResponseEntity.ok(ApiResponse.success(timer))
    }

    @GetMapping("/cooking-mode/{recipeId}")
    fun getCookingMode(@PathVariable recipeId: UUID): ResponseEntity<ApiResponse<CookingModeDto>> {
        val mode = cookingModeService.getCookingMode(recipeId)
        return ResponseEntity.ok(ApiResponse.success(mode))
    }
}

@RestController
@RequestMapping("/api/v1/voice")
class VoiceController(
    private val voiceService: VoiceService
) {

    @PostMapping("/log-meal")
    fun logMealByVoice(@Valid @RequestBody request: VoiceMealLogRequest): ResponseEntity<ApiResponse<Void>> {
        voiceService.logMealByVoice(request)
        return ResponseEntity.ok(ApiResponse.success(null))
    }

    @GetMapping("/shortcuts")
    fun getVoiceShortcuts(): ResponseEntity<ApiResponse<List<VoiceShortcutDto>>> {
        val shortcuts = voiceService.getVoiceShortcuts()
        return ResponseEntity.ok(ApiResponse.success(shortcuts))
    }
}

@RestController
@RequestMapping("/api/v1/wearable")
class WearableController(
    private val wearableService: WearableService
) {

    @GetMapping("/sync")
    fun syncWearableData(): ResponseEntity<ApiResponse<WearableSyncDto>> {
        val sync = wearableService.syncWearableData()
        return ResponseEntity.ok(ApiResponse.success(sync))
    }

    @GetMapping("/google-fit")
    fun getGoogleFitData(): ResponseEntity<ApiResponse<GoogleFitDto>> {
        val data = wearableService.getGoogleFitData()
        return ResponseEntity.ok(ApiResponse.success(data))
    }

    @GetMapping("/healthkit")
    fun getHealthKitData(): ResponseEntity<ApiResponse<HealthKitDto>> {
        val data = wearableService.getHealthKitData()
        return ResponseEntity.ok(ApiResponse.success(data))
    }
}

data class PrepStepDto(
    val stepNumber: Int,
    val instruction: String,
    val durationMinutes: Int
)

data class MakeAheadMealDto(
    val id: UUID,
    val name: String,
    val storageInstructions: String,
    val expirationDate: String
)

data class CookingTimerRequest(
    val recipeId: UUID,
    val stepIndex: Int,
    val durationSeconds: Int
)

data class TimerDto(
    val timerId: UUID,
    val remainingSeconds: Int
)

data class CookingModeDto(
    val recipeId: UUID,
    val currentStep: Int,
    val handsFree: Boolean
)

data class VoiceMealLogRequest(
    val transcript: String,
    val userId: UUID
)

data class VoiceShortcutDto(
    val phrase: String,
    val action: String
)

data class WearableSyncDto(
    val syncedAt: String,
    val recordsSynced: Int
)

data class GoogleFitDto(
    val steps: Int,
    val calories: Double,
    val activeMinutes: Int
)

data class HealthKitDto(
    val steps: Int,
    val calories: Double,
    val workouts: Int
)
