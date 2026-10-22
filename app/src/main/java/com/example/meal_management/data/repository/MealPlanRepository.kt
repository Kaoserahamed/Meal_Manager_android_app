package com.example.meal_management.data.repository

import com.example.meal_management.data.model.*
import com.example.meal_management.util.Constants.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Repository for meal plan operations.
 * Handles weekly/monthly meal plans, meal assignments and templates with Firestore.
 */
class MealPlanRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    /**
     * Get all meal plans for a user with real-time updates.
     */
    fun getMealPlans(userId: String): Flow<Result<List<MealPlan>>> = callbackFlow {
        val listener = firestore.collection(COLLECTION_MEAL_PLANS)
            .whereEqualTo("userId", userId)
            .orderBy("weekStartDate", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.Error(error))
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val plans = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(MealPlan::class.java)?.copy(id = doc.id)
                    }
                    trySend(Result.Success(plans))
                } else {
                    trySend(Result.Success(emptyList()))
                }
            }
        awaitClose { listener.remove() }
    }

    /**
     * Get a single meal plan by ID with real-time updates.
     */
    fun getMealPlanById(planId: String): Flow<Result<MealPlan>> = callbackFlow {
        val listener = firestore.collection(COLLECTION_MEAL_PLANS).document(planId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.Error(error))
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    val plan = snapshot.toObject(MealPlan::class.java)
                    if (plan != null) {
                        trySend(Result.Success(plan.copy(id = snapshot.id)))
                    } else {
                        trySend(Result.Error(Exception("Failed to parse meal plan data")))
                    }
                } else {
                    trySend(Result.Error(Exception("Meal plan not found")))
                }
            }
        awaitClose { listener.remove() }
    }

    /**
     * Create a new meal plan and return its generated ID.
     */
    suspend fun createMealPlan(plan: MealPlan): Result<String> {
        return try {
            if (plan.name.isBlank()) {
                return Result.Error(Exception("Meal plan name cannot be empty"))
            }
            val docRef = firestore.collection(COLLECTION_MEAL_PLANS).document()
            val newPlan = plan.copy(
                id = docRef.id,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
            firestore.collection(COLLECTION_MEAL_PLANS).document(docRef.id).set(newPlan).await()
            Result.Success(docRef.id)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Update an existing meal plan.
     */
    suspend fun updateMealPlan(plan: MealPlan): Result<Unit> {
        return try {
            if (plan.id.isBlank()) {
                return Result.Error(Exception("Meal plan ID is required"))
            }
            val updated = plan.copy(updatedAt = System.currentTimeMillis())
            firestore.collection(COLLECTION_MEAL_PLANS).document(plan.id).set(updated).await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Delete a meal plan by ID.
     */
    suspend fun deleteMealPlan(planId: String): Result<Unit> {
        return try {
            firestore.collection(COLLECTION_MEAL_PLANS).document(planId).delete().await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Replace the meal assignments of a plan.
     */
    suspend fun updateMeals(planId: String, meals: List<MealAssignment>): Result<Unit> {
        return try {
            val plan = firestore.collection(COLLECTION_MEAL_PLANS).document(planId).get().await()
                .toObject(MealPlan::class.java)
                ?: return Result.Error(Exception("Meal plan not found"))
            val updated = plan.copy(meals = meals, updatedAt = System.currentTimeMillis())
            firestore.collection(COLLECTION_MEAL_PLANS).document(planId).set(updated).await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
