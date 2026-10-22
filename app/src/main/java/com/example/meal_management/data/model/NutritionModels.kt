package com.example.meal_management.data.model

import com.google.firebase.firestore.DocumentId

/**
 * Aggregated daily nutrition totals.
 */
data class DailyTotals(
    val calories: Double = 0.0,
    val protein: Double = 0.0,
    val carbs: Double = 0.0,
    val fat: Double = 0.0
)

/**
 * User-defined nutrition goals (calories and macros).
 */
data class NutritionGoal(
    @DocumentId val id: String = "",
    val userId: String = "",
    val calories: Double = 2000.0,
    val protein: Double = 50.0,
    val carbs: Double = 275.0,
    val fat: Double = 65.0,
    val updatedAt: Long = System.currentTimeMillis()
)

/**
 * A nutrition log for a single day, recording meals eaten and computed totals.
 */
data class NutritionLog(
    @DocumentId val id: String = "",
    val userId: String = "",
    val date: Long = 0L,
    val meals: List<MealAssignment> = emptyList(),
    val totals: DailyTotals = DailyTotals(),
    val createdAt: Long = System.currentTimeMillis()
)
