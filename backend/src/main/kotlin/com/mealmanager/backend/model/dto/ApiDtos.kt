package com.mealmanager.backend.model.dto

data class ApiResponse<T>(
    val success: Boolean,
    val data: T?,
    val message: String?,
    val timestamp: String = java.time.Instant.now().toString()
) {
    companion object {
        fun <T> success(data: T): ApiResponse<T> =
            ApiResponse(success = true, data = data, message = null)
        fun <T> error(message: String): ApiResponse<T> =
            ApiResponse(success = false, data = null, message = message)
    }
}

data class Page<T>(
    val content: List<T>,
    val page: Int,
    val size: Int,
    val total: Long
)

data class RecipeDto(
    val id: java.util.UUID? = null,
    val title: String = "",
    val description: String = "",
    val ingredients: List<IngredientDto> = emptyList(),
    val instructions: List<String> = emptyList(),
    val prepTimeMinutes: Int = 0,
    val cookTimeMinutes: Int = 0,
    val servings: Int = 1,
    val tags: List<String> = emptyList(),
    val nutrition: NutritionInfoDto = NutritionInfoDto(),
    val imageUrl: String = "",
    val createdBy: String? = null,
    val isFavorite: Boolean = false,
    val createdAt: String? = null,
    val updatedAt: String? = null
)

data class IngredientDto(
    val name: String = "",
    val quantity: Double = 0.0,
    val unit: String = ""
)

data class NutritionInfoDto(
    val calories: Double = 0.0,
    val protein: Double = 0.0,
    val carbs: Double = 0.0,
    val fat: Double = 0.0,
    val fiber: Double = 0.0,
    val sugar: Double = 0.0,
    val sodium: Double = 0.0
)

data class MealPlanDto(
    val id: java.util.UUID? = null,
    val userId: String = "",
    val name: String = "",
    val weekStartDate: String = "",
    val meals: List<MealAssignmentDto> = emptyList(),
    val isTemplate: Boolean = false,
    val createdAt: String? = null,
    val updatedAt: String? = null
)

data class MealAssignmentDto(
    val id: java.util.UUID? = null,
    val day: Int = 0,
    val mealType: String = "DINNER",
    val recipeId: java.util.UUID = java.util.UUID.randomUUID(),
    val servings: Int = 1,
    val calories: Double = 0.0,
    val protein: Double = 0.0,
    val carbs: Double = 0.0,
    val fat: Double = 0.0
)

data class MealPlanTemplateDto(
    val id: java.util.UUID? = null,
    val planId: java.util.UUID? = null,
    val name: String = "",
    val recipeIds: List<java.util.UUID> = emptyList(),
    val isPublic: Boolean = false,
    val createdAt: String? = null
)

data class GroceryListDto(
    val id: java.util.UUID? = null,
    val mealPlanId: java.util.UUID = java.util.UUID.randomUUID(),
    val userId: String = "",
    val items: List<GroceryItemDto> = emptyList(),
    val generatedAt: String? = null
)

data class GroceryItemDto(
    val id: java.util.UUID? = null,
    val name: String = "",
    val quantity: Double = 0.0,
    val unit: String = "",
    val category: String = "",
    val aisle: String = "",
    val checked: Boolean = false,
    val notes: String = ""
)

data class NutritionLogDto(
    val id: java.util.UUID? = null,
    val userId: String = "",
    val date: String = "",
    val meals: List<MealAssignmentDto> = emptyList(),
    val totals: DailyTotalsDto = DailyTotalsDto(),
    val createdAt: String? = null
)

data class DailyTotalsDto(
    val calories: Double = 0.0,
    val protein: Double = 0.0,
    val carbs: Double = 0.0,
    val fat: Double = 0.0
)

data class NutritionGoalDto(
    val id: java.util.UUID? = null,
    val userId: String = "",
    val calories: Double = 2000.0,
    val protein: Double = 50.0,
    val carbs: Double = 275.0,
    val fat: Double = 65.0,
    val updatedAt: String? = null
) {
    companion object {
        fun default() = NutritionGoalDto()
    }
}

data class NutritionProgressDto(
    val caloriesPercent: Double = 0.0,
    val proteinPercent: Double = 0.0,
    val carbsPercent: Double = 0.0,
    val fatPercent: Double = 0.0
)

data class NutritionTrendsDto(
    val range: String = "",
    val dailyAverages: Map<String, com.mealmanager.backend.service.DailyAverageDto> = emptyMap()
)

data class ShareTokenDto(
    val token: String = "",
    val recipeId: java.util.UUID = java.util.UUID.randomUUID(),
    val expiresAt: String? = null
)

data class ShareDto(
    val shareId: java.util.UUID = java.util.UUID.randomUUID(),
    val planId: java.util.UUID = java.util.UUID.randomUUID(),
    val sharedWith: List<String> = emptyList(),
    val permissions: List<String> = emptyList(),
    val createdAt: String? = null
)

data class CollectionDto(
    val id: java.util.UUID? = null,
    val userId: String = "",
    val name: String = "",
    val recipeIds: List<java.util.UUID> = emptyList(),
    val isPublic: Boolean = false,
    val createdAt: String? = null
)

data class RatingDto(
    val id: java.util.UUID? = null,
    val recipeId: java.util.UUID = java.util.UUID.randomUUID(),
    val userId: String = "",
    val rating: Int = 5,
    val review: String = "",
    val createdAt: String? = null
)

data class BarcodeScanResult(
    val barcode: String = "",
    val found: Boolean = false,
    val foodName: String? = null,
    val brand: String? = null,
    val nutrition: NutritionInfoDto? = null,
    val ingredients: List<String>? = null
)

data class PackagedFoodDto(
    val id: java.util.UUID? = null,
    val barcode: String = "",
    val name: String = "",
    val brand: String = "",
    val nutrition: NutritionInfoDto = NutritionInfoDto(),
    val ingredients: List<String> = emptyList(),
    val servingSize: String = ""
)

data class FridgeItemDto(
    val id: java.util.UUID? = null,
    val name: String = "",
    val quantity: Double = 0.0,
    val unit: String = "",
    val category: String = "",
    val expirationDate: String? = null,
    val lowStockThreshold: Int = 1,
    val createdAt: String? = null
)

data class PantryItemDto(
    val id: java.util.UUID? = null,
    val name: String = "",
    val quantity: Double = 0.0,
    val unit: String = "",
    val category: String = "",
    val reorderThreshold: Int = 1,
    val estimatedReorderCost: Double = 0.0,
    val createdAt: String? = null
)

data class WeightLogDto(
    val id: java.util.UUID? = null,
    val userId: String = "",
    val date: String = "",
    val weightKg: Double = 0.0,
    val bodyFatPercent: Double? = null,
    val createdAt: String? = null
)

data class WaterLogDto(
    val id: java.util.UUID? = null,
    val userId: String = "",
    val date: String = "",
    val amountMl: Double = 0.0,
    val createdAt: String? = null
)

data class FastingWindowDto(
    val id: java.util.UUID? = null,
    val userId: String = "",
    val startTime: String? = null,
    val endTime: String? = null,
    val durationHours: Double = 0.0,
    val completed: Boolean = false,
    val createdAt: String? = null
)

data class UserDto(
    val id: java.util.UUID? = null,
    val email: String = "",
    val name: String = "",
    val role: String = "member",
    val createdAt: String? = null
)

data class ProfileDto(
    val userId: String = "",
    val displayName: String = "",
    val avatarUrl: String? = null,
    val bio: String? = null,
    val location: String? = null
) {
    companion object {
        fun default(userId: String) = ProfileDto(userId = userId)
    }
}

data class DietaryPreferencesDto(
    val userId: String = "",
    val restrictions: List<String> = emptyList(),
    val preferences: List<String> = emptyList(),
    val goals: List<String> = emptyList(),
    val timezone: String = "UTC"
) {
    companion object {
        fun default(userId: String) = DietaryPreferencesDto(userId = userId)
    }
}

data class AuditLogDto(
    val id: java.util.UUID? = null,
    val userId: String = "",
    val action: String = "",
    val details: String = "",
    val timestamp: String? = null
)
