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
import com.example.meal_management.presentation.screen.LoginScreen
import com.example.meal_management.presentation.screen.SignUpScreen
import com.example.meal_management.presentation.viewmodel.AuthViewModel
import com.example.meal_management.ui.theme.Meal_ManagementTheme
import com.example.meal_management.util.Constants
import com.google.firebase.FirebaseApp

/**
 * Main Activity - Entry point of the application
 * Handles navigation and Firebase initialization
 */
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize Firebase
        FirebaseApp.initializeApp(this)
        
        setContent {
            Meal_ManagementTheme {
                val authViewModel: AuthViewModel by viewModels()
                val navController = rememberNavController()
                
                NavHost(
                    navController = navController,
                    startDestination = Constants.Routes.LOGIN
                ) {
                    // Authentication Screens
                    composable(Constants.Routes.LOGIN) {
                        LoginScreen(
                            navController = navController,
                            authViewModel = authViewModel
                        )
                    }
                    
                    composable(Constants.Routes.SIGNUP) {
                        SignUpScreen(
                            navController = navController,
                            authViewModel = authViewModel
                        )
                    }
                    
                    // Home Screens
                    composable(Constants.Routes.HOME) {
                        HomePage(navController = navController, authViewModel = authViewModel)
                    }
                    
                    composable(Constants.Routes.HOME_MEMBER) {
                        HomePage_Member(navController = navController)
                    }
                    
                    // Mess Management
                    composable(Constants.Routes.CREATE_MESS) {
                        CreateMess(navController = navController)
                    }
                    
                    composable(Constants.Routes.ADD_MEMBER) {
                        Add_Mem(navController = navController)
                    }
                    
                    composable(Constants.Routes.MEMBER_LIST) {
                        MembersList(navController = navController)
                    }
                    
                    composable(Constants.Routes.CHANGE_MANAGER) {
                        ChangeManager(navController = navController)
                    }
                    
                    // Transactions
                    composable(Constants.Routes.ADD_MEAL) {
                        AddMeal(navController = navController)
                    }
                    
                    composable(Constants.Routes.ADD_DEPOSIT) {
                        Deposit(navController = navController)
                    }
                    
                    composable(Constants.Routes.ADD_SHOPPING) {
                        Shopping_Add(navController = navController)
                    }
                    
                    // History
                    composable(Constants.Routes.DEPOSIT_HISTORY) {
                        DepoHistory(navController = navController)
                    }
                    
                    composable(Constants.Routes.SHOPPING_HISTORY) {
                        ShopHistory(navController = navController)
                    }
                    
                    // Communication
                    composable(Constants.Routes.CHAT) {
                        Chat(navController = navController)
                    }
                }
            }
        }
    }
}
