package com.mealmanager.backend.controller

import com.mealmanager.backend.model.dto.ApiResponse
import com.mealmanager.backend.model.dto.MLRecommendationDto
import com.mealmanager.backend.model.dto.PersonalizationDto
import com.mealmanager.backend.service.MLRecommendationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/v1/ml")
class MLController(
    private val mlService: MLRecommendationService
) {

    @GetMapping("/recommendations/recipes")
    fun getRecipeRecommendations(): ResponseEntity<ApiResponse<List<MLRecommendationDto>>> {
        val recs = mlService.getRecipeRecommendations()
        return ResponseEntity.ok(ApiResponse.success(recs))
    }

    @GetMapping("/recommendations/meals")
    fun getMealRecommendations(): ResponseEntity<ApiResponse<List<MLRecommendationDto>>> {
        val recs = mlService.getMealRecommendations()
        return ResponseEntity.ok(ApiResponse.success(recs))
    }

    @PostMapping("/personalization")
    fun updatePersonalization(@Valid @RequestBody dto: PersonalizationDto): ResponseEntity<ApiResponse<PersonalizationDto>> {
        val updated = mlService.updatePersonalization(dto)
        return ResponseEntity.ok(ApiResponse.success(updated))
    }

    @GetMapping("/personalization")
    fun getPersonalization(): ResponseEntity<ApiResponse<PersonalizationDto>> {
        val p = mlService.getPersonalization()
        return ResponseEntity.ok(ApiResponse.success(p))
    }

    @GetMapping("/collaborative-filtering")
    fun getCollaborativeFiltering(): ResponseEntity<ApiResponse<List<MLRecommendationDto>>> {
        val recs = mlService.getCollaborativeFiltering()
        return ResponseEntity.ok(ApiResponse.success(recs))
    }

    @GetMapping("/content-based")
    fun getContentBased(): ResponseEntity<ApiResponse<List<MLRecommendationDto>>> {
        val recs = mlService.getContentBased()
        return ResponseEntity.ok(ApiResponse.success(recs))
    }

    @PostMapping("/train")
    fun trainModel(): ResponseEntity<ApiResponse<ModelTrainingDto>> {
        val result = mlService.trainModel()
        return ResponseEntity.ok(ApiResponse.success(result))
    }
}

import jakarta.validation.Valid

data class ModelTrainingDto(
    val modelId: String = "",
    val status: String = "completed",
    val accuracy: Double = 0.0,
    val trainedAt: String = java.time.Instant.now().toString()
)
