package com.example.meal_management

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.meal_management.presentation.screen.*
import com.example.meal_management.presentation.viewmodel.*
import com.example.meal_management.ui.theme.Meal_ManagementTheme
import com.example.meal_management.util.Constants
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            Meal_ManagementTheme {
                val authViewModel: AuthViewModel by viewModels()
                val recipeViewModel: RecipeViewModel by viewModels()
                val mealPlanViewModel: MealPlanViewModel by viewModels()
                val nutritionViewModel: NutritionViewModel by viewModels()
                val groceryViewModel: GroceryViewModel by viewModels()
                val barcodeViewModel: BarcodeViewModel by viewModels()
                val fridgeViewModel: FridgeViewModel by viewModels()
                val pantryViewModel: PantryViewModel by viewModels()
                val weightViewModel: WeightViewModel by viewModels()
                val waterViewModel: WaterViewModel by viewModels()
                val fastingViewModel: FastingViewModel by viewModels()
                val mealPhotoViewModel: MealPhotoViewModel by viewModels()
                val auditLogViewModel: AuditLogViewModel by viewModels()
                val featureFlagViewModel: FeatureFlagViewModel by viewModels()
                val abTestViewModel: AbTestViewModel by viewModels()
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Constants.Routes.LOGIN) {
                    composable(Constants.Routes.LOGIN) { LoginScreen(navController, authViewModel) }
                    composable(Constants.Routes.SIGNUP) { SignUpScreen(navController, authViewModel) }
                    composable(Constants.Routes.HOME) { HomePage(navController, authViewModel) }
                    composable(Constants.Routes.HOME_MEMBER) { HomePage_Member(navController) }
                    composable(Constants.Routes.CREATE_MESS) { CreateMess(navController) }
                    composable(Constants.Routes.ADD_MEMBER) { Add_Mem(navController) }
                    composable(Constants.Routes.MEMBER_LIST) { MembersList(navController) }
                    composable(Constants.Routes.CHANGE_MANAGER) { ChangeManager(navController) }
                    composable(Constants.Routes.ADD_MEAL) { AddMeal(navController) }
                    composable(Constants.Routes.ADD_DEPOSIT) { Deposit(navController) }
                    composable(Constants.Routes.ADD_SHOPPING) { Shopping_Add(navController) }
                    composable(Constants.Routes.DEPOSIT_HISTORY) { DepoHistory(navController) }
                    composable(Constants.Routes.SHOPPING_HISTORY) { ShopHistory(navController) }
                    composable(Constants.Routes.CHAT) { Chat(navController) }
                    composable(Constants.Routes.RECIPE_LIST) { RecipeListScreen(navController, recipeViewModel) }
                    composable(Constants.Routes.RECIPE_DETAIL) { RecipeDetailScreen(navController, recipeViewModel) }
                    composable(Constants.Routes.RECIPE_FORM) { RecipeFormScreen(navController, recipeViewModel) }
                    composable(Constants.Routes.MEAL_PLAN) { MealPlanScreen(navController, mealPlanViewModel) }
                    composable(Constants.Routes.NUTRITION_DASHBOARD) { NutritionDashboardScreen(navController, nutritionViewModel) }
                    composable(Constants.Routes.GROCERY_LIST) { GroceryListScreen(navController, groceryViewModel) }
                    composable("barcode") { BarcodeScreen(navController, barcodeViewModel) }
                    composable("fridge") { FridgeScreen(navController, fridgeViewModel) }
                    composable("pantry") { PantryScreen(navController, pantryViewModel) }
                    composable("weight") { WeightScreen(navController, weightViewModel) }
                    composable("water") { WaterScreen(navController, waterViewModel) }
                    composable("fasting") { FastingScreen(navController, fastingViewModel) }
                    composable("mealphoto") { MealPhotoScreen(navController, mealPhotoViewModel) }
                    composable("auditlog") { AuditLogScreen(navController, auditLogViewModel) }
                    composable("featureflag") { FeatureFlagScreen(navController, featureFlagViewModel) }
                    composable("abtest") { AbTestScreen(navController, abTestViewModel) }
                }
            }
        }
    }
}
