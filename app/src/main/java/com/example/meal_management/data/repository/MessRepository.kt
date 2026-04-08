package com.example.meal_management.data.repository

import com.example.meal_management.data.model.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Repository for mess-related operations
 * Handles all mess data management with Firestore
 */
class MessRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    
    /**
     * Create a new mess
     */
    suspend fun createMess(name: String, managerId: String): Result<Mess> {
        return try {
            if (name.isBlank()) {
                return Result.Error(Exception("Mess name cannot be empty"))
            }
            
            val mess = Mess(
                name = name,
                managerId = managerId
            )
            
            val docRef = firestore.collection("messes").document()
            firestore.collection("messes").document(docRef.id).set(mess).await()
            
            // Update user's messId
            firestore.collection("users").document(managerId)
                .update(mapOf(
                    "messId" to docRef.id,
                    "isJoined" to true
                )).await()
            
            Result.Success(mess.copy(id = docRef.id))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    /**
     * Get mess by ID with real-time updates
     */
    fun getMessById(messId: String): Flow<Result<Mess>> = callbackFlow {
        val listener = firestore.collection("messes").document(messId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.Error(error))
                    return@addSnapshotListener
                }
                
                if (snapshot != null && snapshot.exists()) {
                    val mess = snapshot.toObject(Mess::class.java)
                    if (mess != null) {
                        trySend(Result.Success(mess.copy(id = snapshot.id)))
                    } else {
                        trySend(Result.Error(Exception("Failed to parse mess data")))
                    }
                } else {
                    trySend(Result.Error(Exception("Mess not found")))
                }
            }
        
        awaitClose { listener.remove() }
    }
    
    /**
     * Get all members of a mess with real-time updates
     */
    fun getMessMembers(messId: String): Flow<Result<List<User>>> = callbackFlow {
        val listener = firestore.collection("users")
            .whereEqualTo("messId", messId)
            .whereEqualTo("isJoined", true)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.Error(error))
                    return@addSnapshotListener
                }
                
                if (snapshot != null) {
                    val users = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(User::class.java)?.copy(id = doc.id)
                    }
                    trySend(Result.Success(users))
                } else {
                    trySend(Result.Success(emptyList()))
                }
            }
        
        awaitClose { listener.remove() }
    }
    
    /**
     * Add deposit for a user
     */
    suspend fun addDeposit(
        messId: String,
        userId: String,
        userName: String,
        amount: Double,
        date: String
    ): Result<Unit> {
        return try {
            if (amount <= 0) {
                return Result.Error(Exception("Amount must be greater than 0"))
            }
            
            firestore.runTransaction { transaction ->
                // Update user's deposit
                val userRef = firestore.collection("users").document(userId)
                val userSnapshot = transaction.get(userRef)
                val currentDeposit = userSnapshot.getDouble("deposit") ?: 0.0
                transaction.update(userRef, "deposit", currentDeposit + amount)
                
                // Update mess total deposit
                val messRef = firestore.collection("messes").document(messId)
                val messSnapshot = transaction.get(messRef)
                val currentTotal = messSnapshot.getDouble("totalDeposit") ?: 0.0
                transaction.update(messRef, "totalDeposit", currentTotal + amount)
                
                // Add to deposit history
                val history = DepositHistory(
                    messId = messId,
                    userId = userId,
                    userName = userName,
                    amount = amount,
                    date = date
                )
                val historyRef = firestore.collection("depositHistory").document()
                transaction.set(historyRef, history)
            }.await()
            
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    /**
     * Add meal for a user
     */
    suspend fun addMeal(
        messId: String,
        userId: String,
        mealCount: Double
    ): Result<Unit> {
        return try {
            if (mealCount <= 0) {
                return Result.Error(Exception("Meal count must be greater than 0"))
            }
            
            firestore.runTransaction { transaction ->
                // Update user's meal count
                val userRef = firestore.collection("users").document(userId)
                val userSnapshot = transaction.get(userRef)
                val currentMeal = userSnapshot.getDouble("mealCount") ?: 0.0
                transaction.update(userRef, "mealCount", currentMeal + mealCount)
                
                // Update mess total meal
                val messRef = firestore.collection("messes").document(messId)
                val messSnapshot = transaction.get(messRef)
                val currentTotal = messSnapshot.getDouble("totalMeal") ?: 0.0
                transaction.update(messRef, "totalMeal", currentTotal + mealCount)
            }.await()
            
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    /**
     * Add shopping expense
     */
    suspend fun addShopping(
        messId: String,
        userId: String,
        userName: String,
        amount: Double,
        items: String,
        date: String
    ): Result<Unit> {
        return try {
            if (amount <= 0) {
                return Result.Error(Exception("Amount must be greater than 0"))
            }
            
            firestore.runTransaction { transaction ->
                // Update mess total shopping
                val messRef = firestore.collection("messes").document(messId)
                val messSnapshot = transaction.get(messRef)
                val currentTotal = messSnapshot.getDouble("totalShopping") ?: 0.0
                transaction.update(messRef, "totalShopping", currentTotal + amount)
                
                // Add to shopping history
                val history = ShoppingHistory(
                    messId = messId,
                    userId = userId,
                    userName = userName,
                    amount = amount,
                    items = items,
                    date = date
                )
                val historyRef = firestore.collection("shoppingHistory").document()
                transaction.set(historyRef, history)
            }.await()
            
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    /**
     * Get deposit history for a mess
     */
    fun getDepositHistory(messId: String): Flow<Result<List<DepositHistory>>> = callbackFlow {
        val listener = firestore.collection("depositHistory")
            .whereEqualTo("messId", messId)
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.Error(error))
                    return@addSnapshotListener
                }
                
                if (snapshot != null) {
                    val history = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(DepositHistory::class.java)?.copy(id = doc.id)
                    }
                    trySend(Result.Success(history))
                } else {
                    trySend(Result.Success(emptyList()))
                }
            }
        
        awaitClose { listener.remove() }
    }
    
    /**
     * Get shopping history for a mess
     */
    fun getShoppingHistory(messId: String): Flow<Result<List<ShoppingHistory>>> = callbackFlow {
        val listener = firestore.collection("shoppingHistory")
            .whereEqualTo("messId", messId)
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.Error(error))
                    return@addSnapshotListener
                }
                
                if (snapshot != null) {
                    val history = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(ShoppingHistory::class.java)?.copy(id = doc.id)
                    }
                    trySend(Result.Success(history))
                } else {
                    trySend(Result.Success(emptyList()))
                }
            }
        
        awaitClose { listener.remove() }
    }
}
