package com.example.meal_management.data.repository

import com.example.meal_management.data.model.Result
import com.example.meal_management.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Repository for authentication operations
 * Handles Firebase Auth and user data management
 */
class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    
    val currentUserId: String?
        get() = auth.currentUser?.uid
    
    val currentUserEmail: String?
        get() = auth.currentUser?.email
    
    val isUserLoggedIn: Boolean
        get() = auth.currentUser != null
    
    /**
     * Sign in with email and password
     */
    suspend fun signIn(email: String, password: String): Result<User> {
        return try {
            // Validate input
            if (!isValidEmail(email)) {
                return Result.Error(Exception("Invalid email format"))
            }
            if (password.length < 6) {
                return Result.Error(Exception("Password must be at least 6 characters"))
            }
            
            // Sign in with Firebase Auth
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val userId = authResult.user?.uid 
                ?: return Result.Error(Exception("User ID not found"))
            
            // Fetch user data from Firestore
            val userDoc = firestore.collection("users").document(userId).get().await()
            if (!userDoc.exists()) {
                return Result.Error(Exception("User data not found"))
            }
            
            val user = userDoc.toObject(User::class.java)
                ?: return Result.Error(Exception("Failed to parse user data"))
            
            Result.Success(user.copy(id = userId))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    /**
     * Sign up with email and password
     */
    suspend fun signUp(
        email: String,
        password: String,
        name: String,
        isManager: Boolean
    ): Result<User> {
        return try {
            // Validate input
            if (!isValidEmail(email)) {
                return Result.Error(Exception("Invalid email format"))
            }
            if (password.length < 6) {
                return Result.Error(Exception("Password must be at least 6 characters"))
            }
            if (name.isBlank()) {
                return Result.Error(Exception("Name cannot be empty"))
            }
            
            // Create user in Firebase Auth
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val userId = authResult.user?.uid 
                ?: return Result.Error(Exception("Failed to create user"))
            
            // Create user document in Firestore
            val user = User(
                id = userId,
                email = email,
                name = name,
                isManager = isManager,
                isJoined = false
            )
            
            firestore.collection("users").document(userId).set(user).await()
            
            Result.Success(user)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    /**
     * Sign out current user
     */
    fun signOut() {
        auth.signOut()
    }
    
    /**
     * Get current user data
     */
    suspend fun getCurrentUser(): Result<User> {
        return try {
            val userId = currentUserId 
                ?: return Result.Error(Exception("No user logged in"))
            
            val userDoc = firestore.collection("users").document(userId).get().await()
            if (!userDoc.exists()) {
                return Result.Error(Exception("User data not found"))
            }
            
            val user = userDoc.toObject(User::class.java)
                ?: return Result.Error(Exception("Failed to parse user data"))
            
            Result.Success(user.copy(id = userId))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    /**
     * Validate email format
     */
    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        return emailRegex.matches(email)
    }
}
