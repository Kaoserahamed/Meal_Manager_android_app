package com.example.meal_management.data.repository

import com.example.meal_management.data.model.*
import com.example.meal_management.util.Constants.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Repository for nutrition operations.
 * Handles nutrition goals, daily logs and macro/calorie tracking with Firestore.
 */
class NutritionRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    /**
     * Get the nutrition goals for a user with real-time updates.
     */
    fun getNutritionGoals(userId: String): Flow<Result<NutritionGoal>> = callbackFlow {
        val listener = firestore.collection(COLLECTION_NUTRITION_GOALS)
            .whereEqualTo("userId", userId)
            .limit(1)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.Error(error))
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.documents.isNotEmpty()) {
                    val doc = snapshot.documents[0]
                    val goal = doc.toObject(NutritionGoal::class.java)?.copy(id = doc.id)
                    if (goal != null) {
                        trySend(Result.Success(goal))
                    } else {
                        trySend(Result.Error(Exception("Failed to parse nutrition goals")))
                    }
                } else {
                    trySend(Result.Error(Exception("Nutrition goals not found")))
                }
            }
        awaitClose { listener.remove() }
    }

    /**
     * Save or update nutrition goals for a user.
     */
    suspend fun saveNutritionGoals(goal: NutritionGoal): Result<Unit> {
        return try {
            if (goal.userId.isBlank()) {
                return Result.Error(Exception("User ID is required"))
            }
            val query = firestore.collection(COLLECTION_NUTRITION_GOALS)
                .whereEqualTo("userId", goal.userId).get().await()
            val docRef = if (query.documents.isNotEmpty()) {
                query.documents[0].reference
            } else {
                firestore.collection(COLLECTION_NUTRITION_GOALS).document()
            }
            val updated = goal.copy(updatedAt = System.currentTimeMillis())
            docRef.set(updated).await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Get nutrition logs for a user within a date range (inclusive), ordered by date.
     */
    fun getNutritionLogs(
        userId: String,
        startDate: Long,
        endDate: Long
    ): Flow<Result<List<NutritionLog>>> = callbackFlow {
        val listener = firestore.collection(COLLECTION_NUTRITION_LOGS)
            .whereEqualTo("userId", userId)
            .whereGreaterThanOrEqualTo("date", startDate)
            .whereLessThanOrEqualTo("date", endDate)
            .orderBy("date", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.Error(error))
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val logs = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(NutritionLog::class.java)?.copy(id = doc.id)
                    }
                    trySend(Result.Success(logs))
                } else {
                    trySend(Result.Success(emptyList()))
                }
            }
        awaitClose { listener.remove() }
    }

    /**
     * Save a nutrition log for a specific day.
     */
    suspend fun saveNutritionLog(log: NutritionLog): Result<String> {
        return try {
            if (log.userId.isBlank()) {
                return Result.Error(Exception("User ID is required"))
            }
            val docRef = firestore.collection(COLLECTION_NUTRITION_LOGS).document()
            val newLog = log.copy(
                id = docRef.id,
                createdAt = System.currentTimeMillis()
            )
            firestore.collection(COLLECTION_NUTRITION_LOGS).document(docRef.id).set(newLog).await()
            Result.Success(docRef.id)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
