package com.example.meal_management.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.meal_management.R
import com.example.meal_management.presentation.viewmodel.AuthState
import com.example.meal_management.presentation.viewmodel.AuthViewModel
import com.example.meal_management.util.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    
    val focusManager = LocalFocusManager.current
    val authState by authViewModel.authState.collectAsState()
    
    // Handle authentication state changes
    LaunchedEffect(authState) {
        when (val state = authState) {
            is AuthState.Authenticated -> {
                // Navigate based on user role
                val route = if (state.user.isManager) {
                    Constants.Routes.HOME
                } else {
                    Constants.Routes.HOME_MEMBER
                }
                navController.navigate(route) {
                    popUpTo(Constants.Routes.LOGIN) { inclusive = true }
                }
            }
            is AuthState.Error -> {
                errorMessage = state.message
                showErrorDialog = true
                authViewModel.resetAuthState()
            }
            else -> {}
        }
    }
    
    val backgroundColor = Color(0, 153, 76)
    val foregroundColor = Color(249, 239, 219)
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable { focusManager.clearFocus() }
            .background(backgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Logo Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp, bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.breakfast),
                contentDescription = "App Logo",
                modifier = Modifier.size(150.dp)
            )
        }
        
        // Form Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .background(foregroundColor, RoundedCornerShape(4.dp))
                .padding(10.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            
            // Email Field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email", color = Color.Black) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(10.dp))
            
            // Password Field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password", color = Color.Black) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                visualTransformation = if (isPasswordVisible) 
                    VisualTransformation.None 
                else 
                    PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            painter = painterResource(
                                id = if (isPasswordVisible) R.drawable.visible 
                                else R.drawable.notvisible2
                            ),
                            contentDescription = if (isPasswordVisible) 
                                "Hide password" 
                            else 
                                "Show password"
                        )
                    }
                },
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Login Button
            Button(
                onClick = {
                    if (email.isBlank() || password.isBlank()) {
                        errorMessage = "Please fill in all fields"
                        showErrorDialog = true
                    } else {
                        focusManager.clearFocus()
                        authViewModel.signIn(email, password)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor),
                shape = RoundedCornerShape(4.dp),
                enabled = authState !is AuthState.Loading
            ) {
                if (authState is AuthState.Loading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text("Log in")
                }
            }
            
            Spacer(modifier = Modifier.height(10.dp))
            
            // Sign Up Button
            Button(
                onClick = {
                    navController.navigate(Constants.Routes.SIGNUP)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text("Sign up")
            }
            
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
    
    // Error Dialog
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text("Error") },
            text = { Text(errorMessage) },
            confirmButton = {
                Button(onClick = { showErrorDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
}
