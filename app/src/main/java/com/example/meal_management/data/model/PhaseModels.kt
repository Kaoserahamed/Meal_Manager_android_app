package com.example.meal_management.data.model

import com.google.firebase.firestore.DocumentId

data class BarcodeScanResult(
    @DocumentId val id: String = "",
    val barcode: String = "",
    val found: Boolean = false,
    val foodName: String? = null,
    val brand: String? = null,
    val nutrition: NutritionInfo? = null,
    val ingredients: List<String> = emptyList(),
    val scannedAt: Long = System.currentTimeMillis()
)

data class PackagedFood(
    @DocumentId val id: String = "",
    val barcode: String = "",
    val name: String = "",
    val brand: String = "",
    val nutrition: NutritionInfo = NutritionInfo(),
    val ingredients: List<String> = emptyList(),
    val servingSize: String = ""
)

data class FridgeItem(
    @DocumentId val id: String = "",
    val name: String = "",
    val quantity: Double = 0.0,
    val unit: String = "",
    val category: String = "",
    val expirationDate: Long = 0L,
    val lowStockThreshold: Int = 1
)

data class PantryItem(
    @DocumentId val id: String = "",
    val name: String = "",
    val quantity: Double = 0.0,
    val unit: String = "",
    val category: String = "",
    val reorderThreshold: Int = 1,
    val estimatedReorderCost: Double = 0.0
)

data class WeightLog(
    @DocumentId val id: String = "",
    val userId: String = "",
    val date: Long = 0L,
    val weightKg: Double = 0.0,
    val bodyFatPercent: Double? = null
)

data class WaterLog(
    @DocumentId val id: String = "",
    val userId: String = "",
    val date: Long = 0L,
    val amountMl: Double = 0.0
)

data class FastingWindow(
    @DocumentId val id: String = "",
    val userId: String = "",
    val startTime: Long = 0L,
    val endTime: Long = 0L,
    val durationHours: Double = 0.0,
    val completed: Boolean = false
)

data class MealPhoto(
    @DocumentId val id: String = "",
    val userId: String = "",
    val photoUrl: String = "",
    val recognizedFood: String? = null,
    val confidence: Double = 0.0,
    val loggedAt: Long = System.currentTimeMillis()
)

data class AIFoodRecognitionResult(
    val foodName: String = "",
    val confidence: Double = 0.0,
    val estimatedPortionGrams: Double = 0.0,
    val nutrition: NutritionInfo = NutritionInfo(),
    val suggestedRecipeIds: List<String> = emptyList()
)

data class SmartSuggestion(
    val itemName: String = "",
    val suggestedRecipeIds: List<String> = emptyList(),
    val reason: String = ""
)

data class PrepStep(
    val stepNumber: Int = 0,
    val instruction: String = "",
    val durationMinutes: Int = 0
)

data class MakeAheadMeal(
    val id: String = "",
    val name: String = "",
    val storageInstructions: String = "",
    val expirationDate: String = ""
)

data class CookingTimer(
    val id: String = "",
    val recipeId: String = "",
    val stepIndex: Int = 0,
    val durationSeconds: Int = 0,
    val remainingSeconds: Int = 0,
    val running: Boolean = false
)

data class CookingMode(
    val recipeId: String = "",
    val currentStep: Int = 0,
    val handsFree: Boolean = false
)

data class VoiceShortcut(
    val phrase: String = "",
    val action: String = ""
)

data class WearableSync(
    val syncedAt: Long = System.currentTimeMillis(),
    val recordsSynced: Int = 0
)

data class GoogleFitData(
    val steps: Int = 0,
    val calories: Double = 0.0,
    val activeMinutes: Int = 0
)

data class HealthKitData(
    val steps: Int = 0,
    val calories: Double = 0.0,
    val workouts: Int = 0
)

data class AuditLog(
    @DocumentId val id: String = "",
    val userId: String = "",
    val action: String = "",
    val details: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

data class FeatureFlag(
    @DocumentId val id: String = "",
    val key: String = "",
    val enabled: Boolean = false,
    val description: String = ""
)

data class AbTestExperiment(
    @DocumentId val id: String = "",
    val name: String = "",
    val status: String = "draft",
    val variants: List<String> = emptyList()
)

data class AbTestAssignment(
    @DocumentId val id: String = "",
    val experimentId: String = "",
    val userId: String = "",
    val variant: String = ""
)

data class AbTestResult(
    @DocumentId val id: String = "",
    val experimentId: String = "",
    val userId: String = "",
    val variant: String = "",
    val converted: Boolean = false
)

data class TranslationEntry(
    val key: String = "",
    val value: String = "",
    val locale: String = "en"
)
