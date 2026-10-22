package com.example.meal_management.data.repository

import com.example.meal_management.data.model.*
import com.example.meal_management.util.Constants.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Repository for grocery list operations.
 * Handles grocery list generation from meal plans, item management and categorization with Firestore.
 */
class GroceryRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val recipeRepository: RecipeRepository = RecipeRepository(firestore)
) {

    /**
     * Get all grocery lists for a user with real-time updates.
     */
    fun getGroceryLists(userId: String): Flow<Result<List<GroceryList>>> = callbackFlow {
        val listener = firestore.collection(COLLECTION_GROCERY_LISTS)
            .whereEqualTo("userId", userId)
            .orderBy("generatedAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.Error(error))
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val lists = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(GroceryList::class.java)?.copy(id = doc.id)
                    }
                    trySend(Result.Success(lists))
                } else {
                    trySend(Result.Success(emptyList()))
                }
            }
        awaitClose { listener.remove() }
    }

    /**
     * Get a single grocery list by ID with real-time updates.
     */
    fun getGroceryListById(listId: String): Flow<Result<GroceryList>> = callbackFlow {
        val listener = firestore.collection(COLLECTION_GROCERY_LISTS).document(listId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.Error(error))
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    val list = snapshot.toObject(GroceryList::class.java)
                    if (list != null) {
                        trySend(Result.Success(list.copy(id = snapshot.id)))
                    } else {
                        trySend(Result.Error(Exception("Failed to parse grocery list data")))
                    }
                } else {
                    trySend(Result.Error(Exception("Grocery list not found")))
                }
            }
        awaitClose { listener.remove() }
    }

    /**
     * Generate a grocery list from a meal plan by aggregating ingredients across
     * all assigned recipes, scaled by servings, categorized and aisle-sorted.
     */
    suspend fun generateGroceryList(plan: MealPlan, userId: String): Result<GroceryList> {
        return try {
            if (plan.id.isBlank()) {
                return Result.Error(Exception("Meal plan ID is required"))
            }

            // Aggregate ingredient quantities across all meal assignments
            val aggregated = mutableMapOf<String, MutableList<Ingredient>>()
            plan.meals.forEach { assignment ->
                val recipeDoc = firestore.collection(COLLECTION_RECIPES)
                    .document(assignment.recipeId).get().await()
                val recipe = recipeDoc.toObject(Recipe::class.java)
                    ?: return@forEach
                val scale = assignment.servings.toDouble() / if (recipe.servings > 0) recipe.servings else 1
                val scaled = recipe.scaleIngredients(scale)
                scaled.forEach { ingredient ->
                    if (ingredient.name.isNotBlank()) {
                        aggregated.getOrPut(ingredient.name) { mutableListOf() }.add(ingredient)
                    }
                }
            }

            // Merge duplicate ingredients by summing quantities
            val merged = aggregated.entries.map { (name, ingredients) ->
                val totalQty = ingredients.sumOf { it.quantity }
                val unit = ingredients.firstOrNull { it.unit.isNotBlank() }?.unit ?: ""
                GroceryItem(
                    name = name,
                    quantity = totalQty,
                    unit = unit,
                    category = categorize(name),
                    aisle = aisleFor(name)
                )
            }.sortedBy { it.category }

            val docRef = firestore.collection(COLLECTION_GROCERY_LISTS).document()
            val groceryList = GroceryList(
                id = docRef.id,
                userId = userId,
                mealPlanId = plan.id,
                items = merged,
                generatedAt = System.currentTimeMillis()
            )
            firestore.collection(COLLECTION_GROCERY_LISTS).document(docRef.id).set(groceryList).await()
            Result.Success(groceryList)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Save or update a grocery list.
     */
    suspend fun saveGroceryList(list: GroceryList): Result<Unit> {
        return try {
            if (list.userId.isBlank()) {
                return Result.Error(Exception("User ID is required"))
            }
            val updated = list.copy(generatedAt = System.currentTimeMillis())
            firestore.collection(COLLECTION_GROCERY_LISTS)
                .document(if (list.id.isBlank()) firestore.collection(COLLECTION_GROCERY_LISTS).document().id else list.id)
                .set(updated).await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Toggle the checked state of a grocery item.
     */
    suspend fun toggleItemChecked(listId: String, itemId: String, checked: Boolean): Result<Unit> {
        return try {
            firestore.runTransaction { transaction ->
                val listRef = firestore.collection(COLLECTION_GROCERY_LISTS).document(listId)
                val snapshot = transaction.get(listRef)
                val list = snapshot.toObject(GroceryList::class.java)
                    ?: throw Exception("Grocery list not found")
                val updatedItems = list.items.map { item ->
                    if (item.id == itemId) item.copy(checked = checked) else item
                }
                transaction.update(listRef, "items", updatedItems)
            }.await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Delete a grocery list by ID.
     */
    suspend fun deleteGroceryList(listId: String): Result<Unit> {
        return try {
            firestore.collection(COLLECTION_GROCERY_LISTS).document(listId).delete().await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    private fun categorize(name: String): String {
        val lower = name.lowercase()
        return when {
            lower.contains("milk") || lower.contains("cheese") || lower.contains("yogurt") || lower.contains("butter") -> "Dairy"
            lower.contains("apple") || lower.contains("banana") || lower.contains("orange") || lower.contains("berry") || lower.contains("fruit") -> "Produce"
            lower.contains("potato") || lower.contains("tomato") || lower.contains("onion") || lower.contains("carrot") || lower.contains("lettuce") || lower.contains("vegetable") -> "Produce"
            lower.contains("chicken") || lower.contains("beef") || lower.contains("pork") || lower.contains("fish") || lower.contains("meat") || lower.contains("turkey") -> "Meat"
            lower.contains("bread") || lower.contains("rice") || lower.contains("pasta") || lower.contains("flour") || lower.contains("cereal") || lower.contains("grain") -> "Pantry"
            lower.contains("egg") -> "Dairy"
            else -> "Other"
        }
    }

    private fun aisleFor(name: String): String {
        return when (categorize(name)) {
            "Dairy" -> "Aisle 4"
            "Produce" -> "Aisle 1"
            "Meat" -> "Aisle 5"
            "Pantry" -> "Aisle 3"
            else -> "Aisle 6"
        }
    }
}
