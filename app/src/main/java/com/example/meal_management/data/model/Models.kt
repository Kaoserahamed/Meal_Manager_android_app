package com.example.meal_management.data.model

import com.google.firebase.firestore.DocumentId

/**
 * Domain models for the application
 * Using data classes with proper validation and type safety
 */

data class User(
    @DocumentId val id: String = "",
    val email: String = "",
    val name: String = "",
    val messId: String = "",
    val isManager: Boolean = false,
    val isJoined: Boolean = false,
    val deposit: Double = 0.0,
    val mealCount: Double = 0.0,
    val balance: Double = 0.0
) {
    fun calculateBalance(mealRate: Double): Double {
        return deposit - (mealRate * mealCount)
    }
}

data class Mess(
    @DocumentId val id: String = "",
    val name: String = "",
    val managerId: String = "",
    val totalMeal: Double = 0.0,
    val totalDeposit: Double = 0.0,
    val totalShopping: Double = 0.0
) {
    val balance: Double
        get() = totalDeposit - totalShopping
    
    val mealRate: Double
        get() = if (totalMeal > 0) totalShopping / totalMeal else 0.0
}

data class DepositHistory(
    @DocumentId val id: String = "",
    val messId: String = "",
    val userId: String = "",
    val userName: String = "",
    val amount: Double = 0.0,
    val timestamp: Long = System.currentTimeMillis(),
    val date: String = ""
)

data class ShoppingHistory(
    @DocumentId val id: String = "",
    val messId: String = "",
    val userId: String = "",
    val userName: String = "",
    val amount: Double = 0.0,
    val items: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val date: String = ""
)

data class Message(
    @DocumentId val id: String = "",
    val messId: String = "",
    val userId: String = "",
    val userName: String = "",
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

// Result wrapper for handling success/error states
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()
}
