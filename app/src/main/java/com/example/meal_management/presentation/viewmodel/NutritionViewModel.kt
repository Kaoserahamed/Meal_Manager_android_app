package com.example.meal_management.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meal_management.data.model.*
import com.example.meal_management.data.repository.NutritionRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel for nutrition operations.
 * Manages nutrition goals, daily logs and progress tracking.
 */
class NutritionViewModel(
    private val nutritionRepository: NutritionRepository = NutritionRepository()
) : ViewModel() {

    private val _nutritionGoals = MutableStateFlow<NutritionGoal?>(null)
    val nutritionGoals: StateFlow<NutritionGoal?> = _nutritionGoals.asStateFlow()

    private val _nutritionLogs = MutableStateFlow<List<NutritionLog>>(emptyList())
    val nutritionLogs: StateFlow<List<NutritionLog>> = _nutritionLogs.asStateFlow()

    private val _goalState = MutableStateFlow<NutritionState>(NutritionState.Initial)
    val goalState: StateFlow<NutritionState> = _goalState.asStateFlow()

    private val _logState = MutableStateFlow<NutritionState>(NutritionState.Initial)
    val logState: StateFlow<NutritionState> = _logState.asStateFlow()

    /**
     * Load nutrition goals for a user with real-time updates.
     */
    fun loadNutritionGoals(userId: String) {
        viewModelScope.launch {
            _goalState.value = NutritionState.Loading
            nutritionRepository.getNutritionGoals(userId).collect { result ->
                when (result) {
                    is Result.Success -> _nutritionGoals.value = result.data
                    is Result.Error -> _goalState.value = NutritionState.Error(result.exception.message ?: "Failed to load nutrition goals")
                    else -> {}
                }
            }
        }
    }

    /**
     * Save or update nutrition goals for a user.
     */
    fun saveNutritionGoals(
        userId: String,
        calories: Double,
        protein: Double,
        carbs: Double,
        fat: Double
    ) {
        viewModelScope.launch {
            _goalState.value = NutritionState.Loading
            val goal = NutritionGoal(
                userId = userId,
                calories = calories,
                protein = protein,
                carbs = carbs,
                fat = fat
            )
            when (val result = nutritionRepository.saveNutritionGoals(goal)) {
                is Result.Success -> {
                    _goalState.value = NutritionState.Success("Nutrition goals saved")
                    loadNutritionGoals(userId)
                }
                is Result.Error -> _goalState.value = NutritionState.Error(result.exception.message ?: "Failed to save nutrition goals")
                else -> {}
            }
        }
    }

    /**
     * Load nutrition logs for a user within a date range.
     */
    fun loadNutritionLogs(userId: String, startDate: Long, endDate: Long) {
        viewModelScope.launch {
            _logState.value = NutritionState.Loading
            nutritionRepository.getNutritionLogs(userId, startDate, endDate).collect { result ->
                when (result) {
                    is Result.Success -> _nutritionLogs.value = result.data
                    is Result.Error -> _logState.value = NutritionState.Error(result.exception.message ?: "Failed to load nutrition logs")
                    else -> {}
                }
            }
        }
    }

    /**
     * Save a nutrition log for a specific day.
     */
    fun saveNutritionLog(
        userId: String,
        date: Long,
        meals: List<MealAssignment>,
        totals: DailyTotals
    ) {
        viewModelScope.launch {
            _logState.value = NutritionState.Loading
            val log = NutritionLog(
                userId = userId,
                date = date,
                meals = meals,
                totals = totals
            )
            when (val result = nutritionRepository.saveNutritionLog(log)) {
                is Result.Success -> {
                    _logState.value = NutritionState.Success("Nutrition log saved")
                    loadNutritionLogs(userId, date - 86400000, date + 86400000)
                }
                is Result.Error -> _logState.value = NutritionState.Error(result.exception.message ?: "Failed to save nutrition log")
                else -> {}
            }
        }
    }

    /**
     * Compute progress percentages against goals for a set of daily totals.
     */
    fun progress(totals: DailyTotals): DailyProgress {
        val goals = _nutritionGoals.value
        return DailyProgress(
            calories = if (goals?.calories != null && goals.calories > 0) totals.calories / goals.calories * 100 else 0.0,
            protein = if (goals?.protein != null && goals.protein > 0) totals.protein / goals.protein * 100 else 0.0,
            carbs = if (goals?.carbs != null && goals.carbs > 0) totals.carbs / goals.carbs * 100 else 0.0,
            fat = if (goals?.fat != null && goals.fat > 0) totals.fat / goals.fat * 100 else 0.0
        )
    }

    /**
     * Aggregate totals across a list of nutrition logs.
     */
    fun aggregateTotals(logs: List<NutritionLog>): DailyTotals =
        logs.fold(DailyTotals()) { acc, log -> acc + log.totals }

    private operator fun DailyTotals.plus(other: DailyTotals): DailyTotals =
        DailyTotals(
            calories + other.calories,
            protein + other.protein,
            carbs + other.carbs,
            fat + other.fat
        )

    /**
     * Reset nutrition states.
     */
    fun resetNutritionState() {
        _goalState.value = NutritionState.Initial
        _logState.value = NutritionState.Initial
    }
}

/**
 * Progress percentages for each nutrient against goals.
 */
data class DailyProgress(
    val calories: Double = 0.0,
    val protein: Double = 0.0,
    val carbs: Double = 0.0,
    val fat: Double = 0.0
)

/**
 * Sealed class representing nutrition operation states.
 */
sealed class NutritionState {
    object Initial : NutritionState()
    object Loading : NutritionState()
    data class Success(val message: String) : NutritionState()
    data class Error(val message: String) : NutritionState()
}
