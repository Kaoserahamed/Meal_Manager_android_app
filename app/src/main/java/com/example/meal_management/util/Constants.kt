package com.example.meal_management.util

/**
 * Application-wide constants
 */
object Constants {
    
    // Firestore Collections
    const val COLLECTION_USERS = "users"
    const val COLLECTION_MESSES = "messes"
    const val COLLECTION_DEPOSIT_HISTORY = "depositHistory"
    const val COLLECTION_SHOPPING_HISTORY = "shoppingHistory"
    const val COLLECTION_MESSAGES = "messages"
    const val COLLECTION_RECIPES = "recipes"
    const val COLLECTION_MEAL_PLANS = "mealPlans"
    const val COLLECTION_NUTRITION_LOGS = "nutritionLogs"
    const val COLLECTION_NUTRITION_GOALS = "nutritionGoals"
    const val COLLECTION_GROCERY_LISTS = "groceryLists"
    
    // Validation
    const val MIN_PASSWORD_LENGTH = 6
    const val MIN_NAME_LENGTH = 2
    const val MAX_NAME_LENGTH = 50
    
    // Navigation Routes
    object Routes {
        const val LOGIN = "login"
        const val SIGNUP = "signup"
        const val HOME = "home"
        const val HOME_MEMBER = "homemember"
        const val CREATE_MESS = "create"
        const val ADD_MEMBER = "addmem"
        const val ADD_MEAL = "addmeal"
        const val ADD_DEPOSIT = "deposit"
        const val ADD_SHOPPING = "shopping"
        const val MEMBER_LIST = "memlist"
        const val DEPOSIT_HISTORY = "depohis"
        const val SHOPPING_HISTORY = "shophis"
        const val CHAT = "chat"
        const val CHANGE_MANAGER = "change"
        // Meal platform routes
        const val RECIPE_LIST = "recipelist"
        const val RECIPE_DETAIL = "recipedetail"
        const val RECIPE_FORM = "recipeform"
        const val MEAL_PLAN = "mealplan"
        const val NUTRITION_DASHBOARD = "nutrition"
        const val GROCERY_LIST = "grocerylist"
    }
    
    // Error Messages
    object ErrorMessages {
        const val INVALID_EMAIL = "Invalid email format"
        const val WEAK_PASSWORD = "Password must be at least 6 characters with letters and numbers"
        const val EMPTY_FIELD = "This field cannot be empty"
        const val INVALID_AMOUNT = "Please enter a valid amount"
        const val LOGIN_FAILED = "Login failed. Please check your credentials"
        const val SIGNUP_FAILED = "Sign up failed. Please try again"
        const val NETWORK_ERROR = "Network error. Please check your connection"
    }
    
    // Success Messages
    object SuccessMessages {
        const val LOGIN_SUCCESS = "Login successful"
        const val SIGNUP_SUCCESS = "Account created successfully"
        const val DEPOSIT_ADDED = "Deposit added successfully"
        const val MEAL_ADDED = "Meal added successfully"
        const val SHOPPING_ADDED = "Shopping added successfully"
        const val MESS_CREATED = "Mess created successfully"
    }
}
