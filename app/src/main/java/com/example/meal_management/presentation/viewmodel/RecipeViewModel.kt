package com.example.meal_management.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meal_management.data.model.*
import com.example.meal_management.data.repository.RecipeRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel for recipe operations.
 * Manages recipe list, selected recipe and UI state.
 */
class RecipeViewModel(
    private val recipeRepository: RecipeRepository = RecipeRepository()
) : ViewModel() {

    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes.asStateFlow()

    private val _selectedRecipe = MutableStateFlow<Recipe?>(null)
    val selectedRecipe: StateFlow<Recipe?> = _selectedRecipe.asStateFlow()

    private val _recipeState = MutableStateFlow<RecipeState>(RecipeState.Initial)
    val recipeState: StateFlow<RecipeState> = _recipeState.asStateFlow()

    /**
     * Load all recipes with real-time updates.
     */
    fun loadRecipes() {
        viewModelScope.launch {
            _recipeState.value = RecipeState.Loading
            recipeRepository.getRecipes().collect { result ->
                when (result) {
                    is Result.Success -> _recipes.value = result.data
                    is Result.Error -> _recipeState.value = RecipeState.Error(result.exception.message ?: "Failed to load recipes")
                    else -> {}
                }
            }
        }
    }

    /**
     * Load recipes created by a specific user.
     */
    fun loadRecipesByUser(userId: String) {
        viewModelScope.launch {
            _recipeState.value = RecipeState.Loading
            recipeRepository.getRecipesByUser(userId).collect { result ->
                when (result) {
                    is Result.Success -> _recipes.value = result.data
                    is Result.Error -> _recipeState.value = RecipeState.Error(result.exception.message ?: "Failed to load recipes")
                    else -> {}
                }
            }
        }
    }

    /**
     * Load recipes filtered by tag.
     */
    fun loadRecipesByTag(tag: String) {
        viewModelScope.launch {
            _recipeState.value = RecipeState.Loading
            recipeRepository.getRecipesByTag(tag).collect { result ->
                when (result) {
                    is Result.Success -> _recipes.value = result.data
                    is Result.Error -> _recipeState.value = RecipeState.Error(result.exception.message ?: "Failed to load recipes")
                    else -> {}
                }
            }
        }
    }

    /**
     * Load a single recipe by ID with real-time updates.
     */
    fun loadRecipeById(recipeId: String) {
        viewModelScope.launch {
            _recipeState.value = RecipeState.Loading
            recipeRepository.getRecipeById(recipeId).collect { result ->
                when (result) {
                    is Result.Success -> _selectedRecipe.value = result.data
                    is Result.Error -> _recipeState.value = RecipeState.Error(result.exception.message ?: "Failed to load recipe")
                    else -> {}
                }
            }
        }
    }

    /**
     * Create a new recipe.
     */
    fun createRecipe(
        title: String,
        description: String,
        ingredients: List<Ingredient>,
        instructions: List<String>,
        prepTimeMinutes: Int,
        cookTimeMinutes: Int,
        servings: Int,
        tags: List<String>,
        nutrition: NutritionInfo,
        imageUrl: String,
        createdBy: String
    ) {
        viewModelScope.launch {
            _recipeState.value = RecipeState.Loading
            val recipe = Recipe(
                title = title,
                description = description,
                ingredients = ingredients,
                instructions = instructions,
                prepTimeMinutes = prepTimeMinutes,
                cookTimeMinutes = cookTimeMinutes,
                servings = servings,
                tags = tags,
                nutrition = nutrition,
                imageUrl = imageUrl,
                createdBy = createdBy
            )
            when (val result = recipeRepository.createRecipe(recipe)) {
                is Result.Success -> {
                    _recipeState.value = RecipeState.Success("Recipe created")
                    loadRecipes()
                }
                is Result.Error -> _recipeState.value = RecipeState.Error(result.exception.message ?: "Failed to create recipe")
                else -> {}
            }
        }
    }

    /**
     * Update an existing recipe.
     */
    fun updateRecipe(recipe: Recipe) {
        viewModelScope.launch {
            _recipeState.value = RecipeState.Loading
            when (val result = recipeRepository.updateRecipe(recipe)) {
                is Result.Success -> {
                    _recipeState.value = RecipeState.Success("Recipe updated")
                    loadRecipes()
                }
                is Result.Error -> _recipeState.value = RecipeState.Error(result.exception.message ?: "Failed to update recipe")
                else -> {}
            }
        }
    }

    /**
     * Delete a recipe by ID.
     */
    fun deleteRecipe(recipeId: String) {
        viewModelScope.launch {
            _recipeState.value = RecipeState.Loading
            when (val result = recipeRepository.deleteRecipe(recipeId)) {
                is Result.Success -> {
                    _recipeState.value = RecipeState.Success("Recipe deleted")
                    loadRecipes()
                    _selectedRecipe.value = null
                }
                is Result.Error -> _recipeState.value = RecipeState.Error(result.exception.message ?: "Failed to delete recipe")
                else -> {}
            }
        }
    }

    /**
     * Select a recipe for viewing/editing.
     */
    fun selectRecipe(recipe: Recipe?) {
        _selectedRecipe.value = recipe
    }

    /**
     * Filter recipes by tag (show all when tag is empty).
     */
    fun filterByTag(tag: String) {
        if (tag.isBlank()) loadRecipes() else loadRecipesByTag(tag)
    }

    /**
     * Reset recipe state.
     */
    fun resetRecipeState() {
        _recipeState.value = RecipeState.Initial
    }
}

/**
 * Sealed class representing recipe operation states.
 */
sealed class RecipeState {
    object Initial : RecipeState()
    object Loading : RecipeState()
    data class Success(val message: String) : RecipeState()
    data class Error(val message: String) : RecipeState()
}
