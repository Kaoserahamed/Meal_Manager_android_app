package com.example.meal_management.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import com.example.meal_management.data.model.*
import com.example.meal_management.presentation.viewmodel.MealPlanViewModel
import com.example.meal_management.util.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealPlanScreen(
    navController: NavController,
    mealPlanViewModel: MealPlanViewModel = viewModel()
) {
    val plans by mealPlanViewModel.mealPlans.collectAsState()
    val state by mealPlanViewModel.planState.collectAsState()
    val scope = rememberCoroutineScope()
    var userId by remember { mutableStateOf("user_1") }
    var planName by remember { mutableStateOf("") }
    var weekStart by remember { mutableStateOf(System.currentTimeMillis().toString()) }
    var showCreate by remember { mutableStateOf(false) }

    LaunchedEffect(state) {
        if (state is MealPlanState.Success) {
            showCreate = false
            planName = ""
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Meal Plans") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showCreate = !showCreate }) {
                Icon(if (showCreate) Icons.Default.Close else Icons.Default.Add, contentDescription = "Toggle create")
            }
        }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Meal Plans", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = userId,
                onValueChange = { userId = it },
                label = { Text("User ID") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { mealPlanViewModel.loadMealPlans(userId) }, modifier = Modifier.fillMaxWidth()) {
                Text("Load Plans")
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (plans.isEmpty()) {
                Text("No meal plans yet. Create your first plan.", style = MaterialTheme.typography.bodyMedium)
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(plans) { plan ->
                        PlanCard(plan = plan, onClick = {
                            mealPlanViewModel.selectPlan(plan)
                            navController.navigate(Constants.Routes.RECIPE_LIST)
                        })
                    }
                }
            }

            if (showCreate) {
                Spacer(modifier = Modifier.height(16.dp))
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Create Meal Plan", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = planName,
                            onValueChange = { planName = it },
                            label = { Text("Plan name") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = weekStart,
                            onValueChange = { weekStart = it },
                            label = { Text("Week start (epoch ms)") },
                            singleLine = true,
                            keyboardType = KeyboardType.Number,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Button(onClick = { showCreate = false }) { Text("Cancel") }
                            Button(
                                onClick = {
                                    scope.launch {
                                        mealPlanViewModel.createMealPlan(
                                            name = planName,
                                            userId = userId,
                                            weekStartDate = weekStart.toLongOrNull() ?: System.currentTimeMillis(),
                                            meals = emptyList()
                                        )
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(0.6f)
                            ) { Text("Create Plan") }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PlanCard(plan: MealPlan, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(plan.name, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Meals: ${plan.meals.size} • ${if (plan.isTemplate) "Template" else "Plan"}",
                style = MaterialTheme.typography.bodySmall)
        }
    }
}
