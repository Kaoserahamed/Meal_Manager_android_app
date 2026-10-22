package com.example.meal_management.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meal_management.data.model.*
import com.example.meal_management.data.repository.MealPlanRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel for meal plan operations.
 * Manages meal plans, current plan and UI state.
 */
class MealPlanViewModel(
    private val mealPlanRepository: MealPlanRepository = MealPlanRepository()
) : ViewModel() {

    private val _mealPlans = MutableStateFlow<List<MealPlan>>(emptyList())
    val mealPlans: StateFlow<List<MealPlan>> = _mealPlans.asStateFlow()

    private val _currentPlan = MutableStateFlow<MealPlan?>(null)
    val currentPlan: StateFlow<MealPlan?> = _currentPlan.asStateFlow()

    private val _planState = MutableStateFlow<MealPlanState>(MealPlanState.Initial)
    val planState: StateFlow<MealPlanState> = _planState.asStateFlow()

    /**
     * Load all meal plans for a user with real-time updates.
     */
    fun loadMealPlans(userId: String) {
        viewModelScope.launch {
            _planState.value = MealPlanState.Loading
            mealPlanRepository.getMealPlans(userId).collect { result ->
                when (result) {
                    is Result.Success -> _mealPlans.value = result.data
                    is Result.Error -> _planState.value = MealPlanState.Error(result.exception.message ?: "Failed to load meal plans")
                    else -> {}
                }
            }
        }
    }

    /**
     * Load a single meal plan by ID with real-time updates.
     */
    fun loadMealPlanById(planId: String) {
        viewModelScope.launch {
            _planState.value = MealPlanState.Loading
            mealPlanRepository.getMealPlanById(planId).collect { result ->
                when (result) {
                    is Result.Success -> _currentPlan.value = result.data
                    is Result.Error -> _planState.value = MealPlanState.Error(result.exception.message ?: "Failed to load meal plan")
                    else -> {}
                }
            }
        }
    }

    /**
     * Create a new meal plan.
     */
    fun createMealPlan(
        name: String,
        userId: String,
        weekStartDate: Long,
        meals: List<MealAssignment>,
        isTemplate: Boolean = false
    ) {
        viewModelScope.launch {
            _planState.value = MealPlanState.Loading
            val plan = MealPlan(
                name = name,
                userId = userId,
                weekStartDate = weekStartDate,
                meals = meals,
                isTemplate = isTemplate
            )
            when (val result = mealPlanRepository.createMealPlan(plan)) {
                is Result.Success -> {
                    _planState.value = MealPlanState.Success("Meal plan created")
                    loadMealPlans(userId)
                }
                is Result.Error -> _planState.value = MealPlanState.Error(result.exception.message ?: "Failed to create meal plan")
                else -> {}
            }
        }
    }

    /**
     * Update an existing meal plan.
     */
    fun updateMealPlan(plan: MealPlan) {
        viewModelScope.launch {
            _planState.value = MealPlanState.Loading
            when (val result = mealPlanRepository.updateMealPlan(plan)) {
                is Result.Success -> {
                    _planState.value = MealPlanState.Success("Meal plan updated")
                    loadMealPlans(plan.userId)
                }
                is Result.Error -> _planState.value = MealPlanState.Error(result.exception.message ?: "Failed to update meal plan")
                else -> {}
            }
        }
    }

    /**
     * Delete a meal plan by ID.
     */
    fun deleteMealPlan(planId: String, userId: String) {
        viewModelScope.launch {
            _planState.value = MealPlanState.Loading
            when (val result = mealPlanRepository.deleteMealPlan(planId)) {
                is Result.Success -> {
                    _planState.value = MealPlanState.Success("Meal plan deleted")
                    loadMealPlans(userId)
                    _currentPlan.value = null
                }
                is Result.Error -> _planState.value = MealPlanState.Error(result.exception.message ?: "Failed to delete meal plan")
                else -> {}
            }
        }
    }

    /**
     * Update the meal assignments of the current plan.
     */
    fun updateMeals(planId: String, meals: List<MealAssignment>) {
        viewModelScope.launch {
            _planState.value = MealPlanState.Loading
            when (val result = mealPlanRepository.updateMeals(planId, meals)) {
                is Result.Success -> {
                    _planState.value = MealPlanState.Success("Meals updated")
                    val current = _currentPlan.value
                    if (current != null) loadMealPlanById(current.id)
                }
                is Result.Error -> _planState.value = MealPlanState.Error(result.exception.message ?: "Failed to update meals")
                else -> {}
            }
        }
    }

    /**
     * Select a plan for viewing/editing.
     */
    fun selectPlan(plan: MealPlan?) {
        _currentPlan.value = plan
    }

    /**
     * Reset plan state.
     */
    fun resetPlanState() {
        _planState.value = MealPlanState.Initial
    }
}

/**
 * Sealed class representing meal plan operation states.
 */
sealed class MealPlanState {
    object Initial : MealPlanState()
    object Loading : MealPlanState()
    data class Success(val message: String) : MealPlanState()
    data class Error(val message: String) : MealPlanState()
}
