package com.example.meal_management.data.model

import com.google.firebase.firestore.DocumentId

enum class MealType { BREAKFAST, LUNCH, DINNER, SNACK }

/**
 * A single meal assignment within a meal plan.
 */
data class MealAssignment(
    val day: Int,
    val mealType: MealType,
    val recipeId: String,
    val servings: Int = 1
)

/**
 * A weekly or monthly meal plan with meal assignments per day and meal type.
 */
data class MealPlan(
    @DocumentId val id: String = "",
    val userId: String = "",
    val name: String = "",
    val weekStartDate: Long = 0L,
    val meals: List<MealAssignment> = emptyList(),
    val isTemplate: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
