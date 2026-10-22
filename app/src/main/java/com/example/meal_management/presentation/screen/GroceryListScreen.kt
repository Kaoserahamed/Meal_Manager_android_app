package com.example.meal_management.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import com.example.meal_management.data.model.*
import com.example.meal_management.presentation.viewmodel.GroceryViewModel
import com.example.meal_management.util.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroceryListScreen(
    navController: NavController,
    groceryViewModel: GroceryViewModel = viewModel()
) {
    val lists by groceryViewModel.groceryLists.collectAsState()
    val currentList by groceryViewModel.currentList.collectAsState()
    val state by groceryViewModel.listState.collectAsState()
    val scope = rememberCoroutineScope()
    var userId by remember { mutableStateOf("user_1") }

    LaunchedEffect(state) {
        if (state is GroceryListState.Success) {
            groceryViewModel.loadGroceryLists(userId)
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Grocery Lists") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(Constants.Routes.RECIPE_LIST) }) {
                Icon(Icons.Default.Shopping_cart, contentDescription = "Generate from recipes")
            }
        }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Grocery Lists", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                OutlinedTextField(
                    value = userId,
                    onValueChange = { userId = it },
                    label = { Text("User ID") },
                    singleLine = true,
                    modifier = Modifier.width(140.dp)
                )
                Button(onClick = { groceryViewModel.loadGroceryLists(userId) }) { Text("Load Lists") }
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (lists.isEmpty()) {
                Text("No grocery lists yet.", style = MaterialTheme.typography.bodyMedium)
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(lists) { list ->
                        GroceryCard(
                            list = list,
                            onClick = { groceryViewModel.selectList(list) },
                            uncheckedCount = groceryViewModel.uncheckedCount(list)
                        )
                    }
                }
            }

            currentList?.let { list ->
                Spacer(modifier = Modifier.height(20.dp))
                Text("List: ${list.mealPlanId}", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Unchecked: ${groceryViewModel.uncheckedCount(list)}", style = MaterialTheme.typography.bodyMedium)
                    Button(onClick = { scope.launch { groceryViewModel.deleteGroceryList(list.id, list.userId) } }) {
                        Text("Delete")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                val byCategory = groceryViewModel.itemsByCategory(list)
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(byCategory.keys.toList()) { category ->
                        val items = byCategory[category] ?: emptyList()
                        Text(category, style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(vertical = 8.dp))
                        items(items) { item ->
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("${item.name} (${item.quantity} ${item.unit})", style = MaterialTheme.typography.bodyMedium)
                                Checkbox(
                                    checked = item.checked,
                                    onChecked = { checked ->
                                        scope.launch {
                                            groceryViewModel.toggleItemChecked(list.id, item.id, checked)
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GroceryCard(list: GroceryList, uncheckedCount: Int, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("Grocery List", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Items: ${list.items.size} • Unchecked: $uncheckedCount", style = MaterialTheme.typography.bodySmall)
        }
    }
}
