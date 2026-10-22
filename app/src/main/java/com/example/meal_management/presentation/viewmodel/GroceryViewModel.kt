package com.example.meal_management.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meal_management.data.model.*
import com.example.meal_management.data.repository.GroceryRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel for grocery list operations.
 * Manages grocery lists, current list and UI state.
 */
class GroceryViewModel(
    private val groceryRepository: GroceryRepository = GroceryRepository()
) : ViewModel() {

    private val _groceryLists = MutableStateFlow<List<GroceryList>>(emptyList())
    val groceryLists: StateFlow<List<GroceryList>> = _groceryLists.asStateFlow()

    private val _currentList = MutableStateFlow<GroceryList?>(null)
    val currentList: StateFlow<GroceryList?> = _currentList.asStateFlow()

    private val _listState = MutableStateFlow<GroceryListState>(GroceryListState.Initial)
    val listState: StateFlow<GroceryListState> = _listState.asStateFlow()

    /**
     * Load all grocery lists for a user with real-time updates.
     */
    fun loadGroceryLists(userId: String) {
        viewModelScope.launch {
            _listState.value = GroceryListState.Loading
            groceryRepository.getGroceryLists(userId).collect { result ->
                when (result) {
                    is Result.Success -> _groceryLists.value = result.data
                    is Result.Error -> _listState.value = GroceryListState.Error(result.exception.message ?: "Failed to load grocery lists")
                    else -> {}
                }
            }
        }
    }

    /**
     * Load a single grocery list by ID with real-time updates.
     */
    fun loadGroceryListById(listId: String) {
        viewModelScope.launch {
            _listState.value = GroceryListState.Loading
            groceryRepository.getGroceryListById(listId).collect { result ->
                when (result) {
                    is Result.Success -> _currentList.value = result.data
                    is Result.Error -> _listState.value = GroceryListState.Error(result.exception.message ?: "Failed to load grocery list")
                    else -> {}
                }
            }
        }
    }

    /**
     * Generate a grocery list from a meal plan.
     */
    fun generateGroceryList(plan: MealPlan, userId: String) {
        viewModelScope.launch {
            _listState.value = GroceryListState.Loading
            when (val result = groceryRepository.generateGroceryList(plan, userId)) {
                is Result.Success -> {
                    _currentList.value = result.data
                    _listState.value = GroceryListState.Success("Grocery list generated")
                    loadGroceryLists(userId)
                }
                is Result.Error -> _listState.value = GroceryListState.Error(result.exception.message ?: "Failed to generate grocery list")
                else -> {}
            }
        }
    }

    /**
     * Save or update a grocery list.
     */
    fun saveGroceryList(list: GroceryList) {
        viewModelScope.launch {
            _listState.value = GroceryListState.Loading
            when (val result = groceryRepository.saveGroceryList(list)) {
                is Result.Success -> {
                    _listState.value = GroceryListState.Success("Grocery list saved")
                    loadGroceryLists(list.userId)
                }
                is Result.Error -> _listState.value = GroceryListState.Error(result.exception.message ?: "Failed to save grocery list")
                else -> {}
            }
        }
    }

    /**
     * Toggle the checked state of a grocery item.
     */
    fun toggleItemChecked(listId: String, itemId: String, checked: Boolean) {
        viewModelScope.launch {
            when (val result = groceryRepository.toggleItemChecked(listId, itemId, checked)) {
                is Result.Success -> {
                    val current = _currentList.value
                    if (current != null && current.id == listId) {
                        loadGroceryListById(listId)
                    }
                }
                is Result.Error -> _listState.value = GroceryListState.Error(result.exception.message ?: "Failed to update item")
                else -> {}
            }
        }
    }

    /**
     * Delete a grocery list by ID.
     */
    fun deleteGroceryList(listId: String, userId: String) {
        viewModelScope.launch {
            _listState.value = GroceryListState.Loading
            when (val result = groceryRepository.deleteGroceryList(listId)) {
                is Result.Success -> {
                    _listState.value = GroceryListState.Success("Grocery list deleted")
                    loadGroceryLists(userId)
                    _currentList.value = null
                }
                is Result.Error -> _listState.value = GroceryListState.Error(result.exception.message ?: "Failed to delete grocery list")
                else -> {}
            }
        }
    }

    /**
     * Select a grocery list for viewing/editing.
     */
    fun selectList(list: GroceryList?) {
        _currentList.value = list
    }

    /**
     * Items grouped by category for display.
     */
    fun itemsByCategory(list: GroceryList): Map<String, List<GroceryItem>> =
        list.items.groupBy { it.category }

    /**
     * Count of unchecked items.
     */
    fun uncheckedCount(list: GroceryList): Int = list.items.count { !it.checked }

    /**
     * Reset list state.
     */
    fun resetListState() {
        _listState.value = GroceryListState.Initial
    }
}

/**
 * Sealed class representing grocery list operation states.
 */
sealed class GroceryListState {
    object Initial : GroceryListState()
    object Loading : GroceryListState()
    data class Success(val message: String) : GroceryListState()
    data class Error(val message: String) : GroceryListState()
}
