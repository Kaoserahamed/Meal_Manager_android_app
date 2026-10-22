package com.example.meal_management.data.repository

import com.example.meal_management.data.model.*
import com.example.meal_management.util.Constants.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Repository for recipe operations.
 * Handles recipe CRUD, ingredient management and tag-based filtering with Firestore.
 */
class RecipeRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    /**
     * Get all recipes with real-time updates.
     */
    fun getRecipes(): Flow<Result<List<Recipe>>> = callbackFlow {
        val listener = firestore.collection(COLLECTION_RECIPES)
            .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.Error(error))
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val recipes = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(Recipe::class.java)?.copy(id = doc.id)
                    }
                    trySend(Result.Success(recipes))
                } else {
                    trySend(Result.Success(emptyList()))
                }
            }
        awaitClose { listener.remove() }
    }

    /**
     * Get recipes created by a specific user.
     */
    fun getRecipesByUser(userId: String): Flow<Result<List<Recipe>>> = callbackFlow {
        val listener = firestore.collection(COLLECTION_RECIPES)
            .whereEqualTo("createdBy", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.Error(error))
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val recipes = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(Recipe::class.java)?.copy(id = doc.id)
                    }
                    trySend(Result.Success(recipes))
                } else {
                    trySend(Result.Success(emptyList()))
                }
            }
        awaitClose { listener.remove() }
    }

    /**
     * Get recipes filtered by a tag.
     */
    fun getRecipesByTag(tag: String): Flow<Result<List<Recipe>>> = callbackFlow {
        val listener = firestore.collection(COLLECTION_RECIPES)
            .whereArrayContains("tags", tag)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.Error(error))
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val recipes = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(Recipe::class.java)?.copy(id = doc.id)
                    }
                    trySend(Result.Success(recipes))
                } else {
                    trySend(Result.Success(emptyList()))
                }
            }
        awaitClose { listener.remove() }
    }

    /**
     * Get a single recipe by ID with real-time updates.
     */
    fun getRecipeById(recipeId: String): Flow<Result<Recipe>> = callbackFlow {
        val listener = firestore.collection(COLLECTION_RECIPES).document(recipeId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.Error(error))
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    val recipe = snapshot.toObject(Recipe::class.java)
                    if (recipe != null) {
                        trySend(Result.Success(recipe.copy(id = snapshot.id)))
                    } else {
                        trySend(Result.Error(Exception("Failed to parse recipe data")))
                    }
                } else {
                    trySend(Result.Error(Exception("Recipe not found")))
                }
            }
        awaitClose { listener.remove() }
    }

    /**
     * Create a new recipe and return its generated ID.
     */
    suspend fun createRecipe(recipe: Recipe): Result<String> {
        return try {
            if (recipe.title.isBlank()) {
                return Result.Error(Exception("Recipe title cannot be empty"))
            }
            val docRef = firestore.collection(COLLECTION_RECIPES).document()
            val newRecipe = recipe.copy(
                id = docRef.id,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
            firestore.collection(COLLECTION_RECIPES).document(docRef.id).set(newRecipe).await()
            Result.Success(docRef.id)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Update an existing recipe.
     */
    suspend fun updateRecipe(recipe: Recipe): Result<Unit> {
        return try {
            if (recipe.id.isBlank()) {
                return Result.Error(Exception("Recipe ID is required"))
            }
            val updated = recipe.copy(updatedAt = System.currentTimeMillis())
            firestore.collection(COLLECTION_RECIPES).document(recipe.id).set(updated).await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Delete a recipe by ID.
     */
    suspend fun deleteRecipe(recipeId: String): Result<Unit> {
        return try {
            firestore.collection(COLLECTION_RECIPES).document(recipeId).delete().await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
