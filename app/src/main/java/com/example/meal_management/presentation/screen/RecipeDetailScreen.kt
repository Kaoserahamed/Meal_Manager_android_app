package com.example.meal_management.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.meal_management.data.model.*
import com.example.meal_management.presentation.viewmodel.RecipeViewModel
import com.example.meal_management.util.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(
    navController: NavController,
    recipeViewModel: RecipeViewModel = viewModel()
) {
    val recipe by recipeViewModel.selectedRecipe.collectAsState()
    val state by recipeViewModel.recipeState.collectAsState()
    var scaleServings by remember { mutableStateOf(1) }

    if (recipe == null) {
        Scaffold(
            topBar = { TopAppBar(title = { Text("Recipe") }) },
            floatingActionButton = {
                FloatingActionButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }
        ) { Text("No recipe selected", modifier = Modifier.fillMaxSize()) }
        return
    }

    val perServing = recipe.nutritionPerServing()
    val scaledIngredients = recipe.scaleIngredients(scaleServings.toDouble() / recipe.servings.coerceAtLeast(1).toDouble())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(recipe.title) },
                actions = {
                    IconButton(onClick = {
                        recipeViewModel.selectRecipe(recipe.copy(servings = scaleServings))
                        navController.navigate(Constants.Routes.RECIPE_FORM)
                    }) { Icon(Icons.Default.Edit, contentDescription = "Edit recipe") }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }
        }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(recipe.description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("Prep", style = MaterialTheme.typography.labelSmall)
                    Text("${recipe.prepTimeMinutes} min", style = MaterialTheme.typography.titleMedium)
                }
                Column {
                    Text("Cook", style = MaterialTheme.typography.labelSmall)
                    Text("${recipe.cookTimeMinutes} min", style = MaterialTheme.typography.titleMedium)
                }
                Column {
                    Text("Servings", style = MaterialTheme.typography.labelSmall)
                    TextField(
                        value = scaleServings.toString(),
                        onValueChange = { scaleServings = it.toIntOrNull() ?: scaleServings },
                        singleLine = true,
                        modifier = Modifier.width(60.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text("Nutrition per serving", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                NutritionChip("Calories", "${perServing.calories.roundToInt()} kcal")
                NutritionChip("Protein", "${perServing.protein.roundToInt()} g")
                NutritionChip("Carbs", "${perServing.carbs.roundToInt()} g")
                NutritionChip("Fat", "${perServing.fat.roundToInt()} g")
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text("Ingredients", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(scaledIngredients) { ingredient ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(ingredient.name, style = MaterialTheme.typography.bodyMedium)
                        Text("${ingredient.quantity.roundToInt()} ${ingredient.unit}", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text("Instructions", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            recipe.instructions.withIndex().forEach { (index, step) ->
                Text("${index + 1}. $step", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

@Composable
private fun NutritionChip(label: String, value: String) {
    Chip(
        label = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(label, style = MaterialTheme.typography.labelSmall)
                Text(value, style = MaterialTheme.typography.titleMedium)
            }
        }
    )
}
