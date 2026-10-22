package com.example.meal_management.data.model

import com.google.firebase.firestore.DocumentId

/**
 * Ingredient for a recipe with name, quantity and unit of measure.
 */
data class Ingredient(
    val name: String = "",
    val quantity: Double = 0.0,
    val unit: String = ""
) {
    fun scaledQuantity(scaleFactor: Double): Double = quantity * scaleFactor
}

/**
 * Nutrition information for a recipe, meal or daily totals.
 */
data class NutritionInfo(
    val calories: Double = 0.0,
    val protein: Double = 0.0,
    val carbs: Double = 0.0,
    val fat: Double = 0.0,
    val fiber: Double = 0.0,
    val sugar: Double = 0.0,
    val sodium: Double = 0.0
) {
    operator fun plus(other: NutritionInfo): NutritionInfo =
        NutritionInfo(
            calories + other.calories,
            protein + other.protein,
            carbs + other.carbs,
            fat + other.fat,
            fiber + other.fiber,
            sugar + other.sugar,
            sodium + other.sodium
        )

    fun divided(by: Double): NutritionInfo {
        val divisor = if (by > 0) by else 1.0
        return NutritionInfo(
            calories / divisor,
            protein / divisor,
            carbs / divisor,
            fat / divisor,
            fiber / divisor,
            sugar / divisor,
            sodium / divisor
        )
    }
}

/**
 * A recipe with ingredients, instructions, timing, servings, tags and nutrition.
 */
data class Recipe(
    @DocumentId val id: String = "",
    val title: String = "",
    val description: String = "",
    val ingredients: List<Ingredient> = emptyList(),
    val instructions: List<String> = emptyList(),
    val prepTimeMinutes: Int = 0,
    val cookTimeMinutes: Int = 0,
    val servings: Int = 1,
    val tags: List<String> = emptyList(),
    val nutrition: NutritionInfo = NutritionInfo(),
    val imageUrl: String = "",
    val createdBy: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    val totalTimeMinutes: Int get() = prepTimeMinutes + cookTimeMinutes

    fun nutritionPerServing(): NutritionInfo =
        nutrition.divided(if (servings > 0) servings.toDouble() else 1.0)

    fun scaleIngredients(scaleFactor: Double): List<Ingredient> =
        ingredients.map { it.copy(quantity = it.scaledQuantity(scaleFactor)) }
}
