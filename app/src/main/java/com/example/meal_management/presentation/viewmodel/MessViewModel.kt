package com.example.meal_management.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meal_management.data.model.*
import com.example.meal_management.data.repository.MessRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel for mess operations
 * Manages mess data and coordinates with MessRepository
 */
class MessViewModel(
    private val messRepository: MessRepository = MessRepository()
) : ViewModel() {
    
    private val _messState = MutableStateFlow<MessState>(MessState.Initial)
    val messState: StateFlow<MessState> = _messState.asStateFlow()
    
    private val _currentMess = MutableStateFlow<Mess?>(null)
    val currentMess: StateFlow<Mess?> = _currentMess.asStateFlow()
    
    private val _members = MutableStateFlow<List<User>>(emptyList())
    val members: StateFlow<List<User>> = _members.asStateFlow()
    
    private val _depositHistory = MutableStateFlow<List<DepositHistory>>(emptyList())
    val depositHistory: StateFlow<List<DepositHistory>> = _depositHistory.asStateFlow()
    
    private val _shoppingHistory = MutableStateFlow<List<ShoppingHistory>>(emptyList())
    val shoppingHistory: StateFlow<List<ShoppingHistory>> = _shoppingHistory.asStateFlow()
    
    /**
     * Create a new mess
     */
    fun createMess(name: String, managerId: String) {
        viewModelScope.launch {
            _messState.value = MessState.Loading
            
            when (val result = messRepository.createMess(name, managerId)) {
                is Result.Success -> {
                    _currentMess.value = result.data
                    _messState.value = MessState.Success("Mess created successfully")
                    loadMessData(result.data.id)
                }
                is Result.Error -> {
                    _messState.value = MessState.Error(result.exception.message ?: "Failed to create mess")
                }
                else -> {}
            }
        }
    }
    
    /**
     * Load mess data by ID
     */
    fun loadMessData(messId: String) {
        viewModelScope.launch {
            // Load mess details
            messRepository.getMessById(messId).collect { result ->
                when (result) {
                    is Result.Success -> _currentMess.value = result.data
                    is Result.Error -> _messState.value = MessState.Error(result.exception.message ?: "Failed to load mess")
                    else -> {}
                }
            }
        }
        
        viewModelScope.launch {
            // Load members
            messRepository.getMessMembers(messId).collect { result ->
                when (result) {
                    is Result.Success -> _members.value = result.data
                    is Result.Error -> _messState.value = MessState.Error(result.exception.message ?: "Failed to load members")
                    else -> {}
                }
            }
        }
        
        viewModelScope.launch {
            // Load deposit history
            messRepository.getDepositHistory(messId).collect { result ->
                when (result) {
                    is Result.Success -> _depositHistory.value = result.data
                    else -> {}
                }
            }
        }
        
        viewModelScope.launch {
            // Load shopping history
            messRepository.getShoppingHistory(messId).collect { result ->
                when (result) {
                    is Result.Success -> _shoppingHistory.value = result.data
                    else -> {}
                }
            }
        }
    }
    
    /**
     * Add deposit for a user
     */
    fun addDeposit(messId: String, userId: String, userName: String, amount: Double, date: String) {
        viewModelScope.launch {
            _messState.value = MessState.Loading
            
            when (val result = messRepository.addDeposit(messId, userId, userName, amount, date)) {
                is Result.Success -> {
                    _messState.value = MessState.Success("Deposit added successfully")
                }
                is Result.Error -> {
                    _messState.value = MessState.Error(result.exception.message ?: "Failed to add deposit")
                }
                else -> {}
            }
        }
    }
    
    /**
     * Add meal for a user
     */
    fun addMeal(messId: String, userId: String, mealCount: Double) {
        viewModelScope.launch {
            _messState.value = MessState.Loading
            
            when (val result = messRepository.addMeal(messId, userId, mealCount)) {
                is Result.Success -> {
                    _messState.value = MessState.Success("Meal added successfully")
                }
                is Result.Error -> {
                    _messState.value = MessState.Error(result.exception.message ?: "Failed to add meal")
                }
                else -> {}
            }
        }
    }
    
    /**
     * Add shopping expense
     */
    fun addShopping(messId: String, userId: String, userName: String, amount: Double, items: String, date: String) {
        viewModelScope.launch {
            _messState.value = MessState.Loading
            
            when (val result = messRepository.addShopping(messId, userId, userName, amount, items, date)) {
                is Result.Success -> {
                    _messState.value = MessState.Success("Shopping added successfully")
                }
                is Result.Error -> {
                    _messState.value = MessState.Error(result.exception.message ?: "Failed to add shopping")
                }
                else -> {}
            }
        }
    }
    
    /**
     * Get member by ID
     */
    fun getMemberById(userId: String): User? {
        return _members.value.find { it.id == userId }
    }
    
    /**
     * Calculate balance for a member
     */
    fun calculateMemberBalance(userId: String): Double {
        val member = getMemberById(userId) ?: return 0.0
        val mealRate = _currentMess.value?.mealRate ?: 0.0
        return member.calculateBalance(mealRate)
    }
    
    /**
     * Reset mess state
     */
    fun resetMessState() {
        _messState.value = MessState.Initial
    }
}

/**
 * Sealed class representing mess operation states
 */
sealed class MessState {
    object Initial : MessState()
    object Loading : MessState()
    data class Success(val message: String) : MessState()
    data class Error(val message: String) : MessState()
}
