package com.example.meal_management.util

/**
 * Utility functions for input validation
 */
object ValidationUtils {
    
    /**
     * Validate email format
     */
    fun isValidEmail(email: String): Boolean {
        if (email.isBlank()) return false
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        return emailRegex.matches(email)
    }
    
    /**
     * Validate password strength
     * Minimum 6 characters, at least one letter and one number
     */
    fun isValidPassword(password: String): Boolean {
        if (password.length < 6) return false
        val hasLetter = password.any { it.isLetter() }
        val hasDigit = password.any { it.isDigit() }
        return hasLetter && hasDigit
    }
    
    /**
     * Validate name
     */
    fun isValidName(name: String): Boolean {
        return name.isNotBlank() && name.length >= 2
    }
    
    /**
     * Validate positive number
     */
    fun isValidPositiveNumber(value: String): Boolean {
        return try {
            val number = value.toDouble()
            number > 0
        } catch (e: NumberFormatException) {
            false
        }
    }
    
    /**
     * Validate integer
     */
    fun isValidInteger(value: String): Boolean {
        return try {
            value.toInt()
            true
        } catch (e: NumberFormatException) {
            false
        }
    }
    
    /**
     * Get password strength message
     */
    fun getPasswordStrengthMessage(password: String): String {
        return when {
            password.length < 6 -> "Password must be at least 6 characters"
            !password.any { it.isLetter() } -> "Password must contain at least one letter"
            !password.any { it.isDigit() } -> "Password must contain at least one number"
            else -> "Strong password"
        }
    }
}
