package com.example.meal_management.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meal_management.data.model.Result
import com.example.meal_management.data.model.User
import com.example.meal_management.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for authentication operations
 * Manages UI state and coordinates with AuthRepository
 */
class AuthViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {
    
    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()
    
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()
    
    init {
        checkAuthStatus()
    }
    
    /**
     * Check if user is already logged in
     */
    private fun checkAuthStatus() {
        if (authRepository.isUserLoggedIn) {
            viewModelScope.launch {
                when (val result = authRepository.getCurrentUser()) {
                    is Result.Success -> {
                        _currentUser.value = result.data
                        _authState.value = AuthState.Authenticated(result.data)
                    }
                    is Result.Error -> {
                        _authState.value = AuthState.Error(result.exception.message ?: "Unknown error")
                    }
                    else -> {}
                }
            }
        }
    }
    
    /**
     * Sign in with email and password
     */
    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            
            when (val result = authRepository.signIn(email, password)) {
                is Result.Success -> {
                    _currentUser.value = result.data
                    _authState.value = AuthState.Authenticated(result.data)
                }
                is Result.Error -> {
                    _authState.value = AuthState.Error(result.exception.message ?: "Login failed")
                }
                else -> {}
            }
        }
    }
    
    /**
     * Sign up with email and password
     */
    fun signUp(email: String, password: String, name: String, isManager: Boolean) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            
            when (val result = authRepository.signUp(email, password, name, isManager)) {
                is Result.Success -> {
                    _currentUser.value = result.data
                    _authState.value = AuthState.Authenticated(result.data)
                }
                is Result.Error -> {
                    _authState.value = AuthState.Error(result.exception.message ?: "Sign up failed")
                }
                else -> {}
            }
        }
    }
    
    /**
     * Sign out current user
     */
    fun signOut() {
        authRepository.signOut()
        _currentUser.value = null
        _authState.value = AuthState.Initial
    }
    
    /**
     * Reset auth state
     */
    fun resetAuthState() {
        _authState.value = AuthState.Initial
    }
}

/**
 * Sealed class representing authentication states
 */
sealed class AuthState {
    object Initial : AuthState()
    object Loading : AuthState()
    data class Authenticated(val user: User) : AuthState()
    data class Error(val message: String) : AuthState()
}
