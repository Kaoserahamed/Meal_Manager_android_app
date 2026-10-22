package com.example.meal_management.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.meal_management.data.model.*
import com.example.meal_management.presentation.viewmodel.RecipeViewModel
import com.example.meal_management.util.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeListScreen(
    navController: NavController,
    recipeViewModel: RecipeViewModel = viewModel()
) {
    val recipes by recipeViewModel.recipes.collectAsState()
    val state by recipeViewModel.recipeState.collectAsState()
    var selectedTag by remember { mutableStateOf<String?>(null) }
    val allTags = recipes.flatMap { it.tags }.distinct().sorted()

    val filtered = if (selectedTag.isNullOrBlank()) recipes else recipes.filter { it.tags.contains(selectedTag) }

    LaunchedEffect(state) {
        if (state is RecipeState.Error) {
            // Error surfaced via state; UI can observe and show a snackbar if desired.
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Recipes") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                recipeViewModel.selectRecipe(null)
                navController.navigate(Constants.Routes.RECIPE_FORM)
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add recipe")
            }
        }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Recipes", style = MaterialTheme.typography.titleLarge)
                Text("Loaded ${filtered.size} recipes", style = MaterialTheme.typography.labelMedium)
            }
            Spacer(modifier = Modifier.height(12.dp))

            RecipeTagFilter(tags = allTags, selected = selectedTag, onTagSelected = { selectedTag = it })
            Spacer(modifier = Modifier.height(16.dp))

            if (filtered.isEmpty()) {
                Text("No recipes found. Add your first recipe.", style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(filtered) { recipe ->
                        RecipeCard(
                            recipe = recipe,
                            onClick = {
                                recipeViewModel.selectRecipe(recipe)
                                navController.navigate(Constants.Routes.RECIPE_DETAIL)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RecipeTagFilter(
    tags: List<String>,
    selected: String?,
    onTagSelected: (String?) -> Unit
) {
    HorizontalPager(tags, selected, onTagSelected)
}

@Composable
private fun HorizontalPager(
    tags: List<String>,
    selected: String?,
    onTagSelected: (String?) -> Unit
) {
    if (tags.isEmpty()) return
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Chip(
            onClick = { onTagSelected(null) },
            selected = selected == null,
            label = { Text("All") }
        )
        Spacer(modifier = Modifier.width(8.dp))
        tags.forEach { tag ->
            Chip(
                onClick = { onTagSelected(tag) },
                selected = selected == tag,
                label = { Text(tag) }
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@Composable
private fun RecipeCard(recipe: Recipe, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(recipe.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("⏱ ${recipe.totalTimeMinutes} min • ${recipe.servings} servings", style = MaterialTheme.typography.bodySmall)
                Text(recipe.tags.joinToString(", "), style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}
