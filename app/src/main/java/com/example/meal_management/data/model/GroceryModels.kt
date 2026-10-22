package com.example.meal_management.data.model

import com.google.firebase.firestore.DocumentId

/**
 * A single item on a grocery list.
 */
data class GroceryItem(
    @DocumentId val id: String = "",
    val name: String = "",
    val quantity: Double = 0.0,
    val unit: String = "",
    val category: String = "",
    val aisle: String = "",
    val checked: Boolean = false,
    val notes: String = ""
)

/**
 * A grocery list generated from a meal plan, with categorized and aisle-sorted items.
 */
data class GroceryList(
    @DocumentId val id: String = "",
    val userId: String = "",
    val mealPlanId: String = "",
    val items: List<GroceryItem> = emptyList(),
    val generatedAt: Long = System.currentTimeMillis()
)
