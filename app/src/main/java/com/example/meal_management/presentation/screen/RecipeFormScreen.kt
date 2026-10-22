package com.example.meal_management.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import com.example.meal_management.data.model.*
import com.example.meal_management.presentation.viewmodel.RecipeViewModel
import com.example.meal_management.util.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeFormScreen(
    navController: NavController,
    recipeViewModel: RecipeViewModel = viewModel()
) {
    val recipe by recipeViewModel.selectedRecipe.collectAsState()
    val state by recipeViewModel.recipeState.collectAsState()
    val scope = rememberCoroutineScope()

    val isEditing = recipe != null

    var title by remember { mutableStateOf(recipe?.title ?: "") }
    var description by remember { mutableStateOf(recipe?.description ?: "") }
    var prepTime by remember { mutableStateOf(recipe?.prepTimeMinutes.toString() ?: "0") }
    var cookTime by remember { mutableStateOf(recipe?.cookTimeMinutes.toString() ?: "0") }
    var servings by remember { mutableStateOf(recipe?.servings.toString() ?: "1") }
    var tags by remember { mutableStateOf(recipe?.tags.joinToString(", ") ?: "") }
    var imageUrl by remember { mutableStateOf(recipe?.imageUrl ?: "") }
    var ingredientsText by remember {
        mutableStateOf(recipe?.ingredients.joinToString("\n") { "${it.name} (${it.quantity} ${it.unit})" } ?: "")
    }
    var instructionsText by remember { mutableStateOf(recipe?.instructions.joinToString("\n") ?: "") }
    var calories by remember { mutableStateOf(recipe?.nutrition.calories.toString() ?: "0") }
    var protein by remember { mutableStateOf(recipe?.nutrition.protein.toString() ?: "0") }
    var carbs by remember { mutableStateOf(recipe?.nutrition.carbs.toString() ?: "0") }
    var fat by remember { mutableStateOf(recipe?.nutrition.fat.toString() ?: "0") }

    LaunchedEffect(state) {
        if (state is RecipeState.Success) {
            navController.popBackStack()
        }
    }

    val onSave = {
        val parsedIngredients = ingredientsText
            .split("\n")
            .filter { it.isNotBlank() }
            .mapNotNull { line ->
                val parts = line.trim().split(" ", limit = 3)
                if (parts.size < 2) null
                else Ingredient(
                    name = parts[0],
                    quantity = parts[1].toDoubleOrNull() ?: 0.0,
                    unit = if (parts.size > 2) parts[2] else ""
                )
            }
        val parsedInstructions = instructionsText.split("\n").filter { it.isNotBlank() }
        val nutrition = NutritionInfo(
            calories = calories.toDoubleOrNull() ?: 0.0,
            protein = protein.toDoubleOrNull() ?: 0.0,
            carbs = carbs.toDoubleOrNull() ?: 0.0,
            fat = fat.toDoubleOrNull() ?: 0.0
        )
        val recipeTags = tags.split(",").map { it.trim() }.filter { it.isNotBlank() }

        val newRecipe = Recipe(
            title = title,
            description = description,
            ingredients = parsedIngredients,
            instructions = parsedInstructions,
            prepTimeMinutes = prepTime.toIntOrNull() ?: 0,
            cookTimeMinutes = cookTime.toIntOrNull() ?: 0,
            servings = servings.toIntOrNull() ?: 1,
            tags = recipeTags,
            nutrition = nutrition,
            imageUrl = imageUrl,
            createdBy = recipe?.createdBy ?: ""
        )

        scope.launch {
            if (isEditing) {
                recipeViewModel.updateRecipe(newRecipe.copy(id = recipe!!.id))
            } else {
                recipeViewModel.createRecipe(
                    title = newRecipe.title,
                    description = newRecipe.description,
                    ingredients = newRecipe.ingredients,
                    instructions = newRecipe.instructions,
                    prepTimeMinutes = newRecipe.prepTimeMinutes,
                    cookTimeMinutes = newRecipe.cookTimeMinutes,
                    servings = newRecipe.servings,
                    tags = newRecipe.tags,
                    nutrition = newRecipe.nutrition,
                    imageUrl = newRecipe.imageUrl,
                    createdBy = newRecipe.createdBy
                )
            }
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text(if (isEditing) "Edit Recipe" else "New Recipe") }) }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            TextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Recipe title") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            TextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                TimeField("Prep time (min)", prepTime, { prepTime = it })
                TimeField("Cook time (min)", cookTime, { cookTime = it })
                TimeField("Servings", servings, { servings = it }, keyboardType = KeyboardType.Number)
            }
            Spacer(modifier = Modifier.height(12.dp))

            TextField(
                value = tags,
                onValueChange = { tags = it },
                label = { Text("Tags (comma separated)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            TextField(
                value = imageUrl,
                onValueChange = { imageUrl = it },
                label = { Text("Image URL (optional)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            Text("Ingredients (one per line: name qty unit)", style = MaterialTheme.typography.labelMedium)
            Spacer(modifier = Modifier.height(4.dp))
            TextField(
                value = ingredientsText,
                onValueChange = { ingredientsText = it },
                label = { Text("") },
                modifier = Modifier.fillMaxWidth().height(100.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text("Instructions (one per line)", style = MaterialTheme.typography.labelMedium)
            Spacer(modifier = Modifier.height(4.dp))
            TextField(
                value = instructionsText,
                onValueChange = { instructionsText = it },
                label = { Text("") },
                modifier = Modifier.fillMaxWidth().height(100.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))

            Text("Nutrition (per full recipe)", style = MaterialTheme.typography.labelMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                NumberField("Calories", calories, { calories = it })
                NumberField("Protein (g)", protein, { protein = it })
                NumberField("Carbs (g)", carbs, { carbs = it })
                NumberField("Fat (g)", fat, { fat = it })
            }
            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onSave,
                modifier = Modifier.fillMaxWidth(),
                enabled = title.isNotBlank()
            ) {
                Text(if (isEditing) "Update Recipe" else "Create Recipe")
            }
        }
    }
}

@Composable
private fun TimeField(label: String, value: String, onValueChange: (String) -> Unit, keyboardType: KeyboardType = KeyboardType.Number) {
    Column {
        Text(label, style = MaterialTheme.typography.labelSmall)
        TextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            keyboardType = keyboardType,
            modifier = Modifier.width(90.dp)
        )
    }
}

@Composable
private fun NumberField(label: String, value: String, onValueChange: (String) -> Unit) {
    Column {
        Text(label, style = MaterialTheme.typography.labelSmall)
        TextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            keyboardType = KeyboardType.Number,
            modifier = Modifier.width(70.dp)
        )
    }
}
