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
import com.example.meal_management.presentation.viewmodel.NutritionViewModel
import com.example.meal_management.util.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NutritionDashboardScreen(
    navController: NavController,
    nutritionViewModel: NutritionViewModel = viewModel()
) {
    val goals by nutritionViewModel.nutritionGoals.collectAsState()
    val logs by nutritionViewModel.nutritionLogs.collectAsState()
    val goalState by nutritionViewModel.goalState.collectAsState()
    val scope = rememberCoroutineScope()
    var userId by remember { mutableStateOf("user_1") }

    var cals by remember { mutableStateOf(goals?.calories.toString() ?: "2000") }
    var prot by remember { mutableStateOf(goals?.protein.toString() ?: "50") }
    var carb by remember { mutableStateOf(goals?.carbs.toString() ?: "275") }
    var fati by remember { mutableStateOf(goals?.fat.toString() ?: "65") }

    val latestTotals = logs.firstOrNull()?.totals ?: DailyTotals()
    val progress = nutritionViewModel.progress(latestTotals)

    LaunchedEffect(goalState) {
        if (goalState is NutritionState.Success) {
            cals = goals?.calories.toString() ?: "2000"
            prot = goals?.protein.toString() ?: "50"
            carb = goals?.carbs.toString() ?: "275"
            fati = goals?.fat.toString() ?: "65"
        }
    }

    val todayStart = System.currentTimeMillis() - (System.currentTimeMillis() % 86400000)

    Scaffold(
        topBar = { TopAppBar(title = { Text("Nutrition") }) }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Nutrition Dashboard", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                OutlinedTextField(
                    value = userId,
                    onValueChange = { userId = it },
                    label = { Text("User ID") },
                    singleLine = true,
                    modifier = Modifier.width(140.dp)
                )
                Button(onClick = { nutritionViewModel.loadNutritionGoals(userId) }) { Text("Load Goals") }
            }
            Spacer(modifier = Modifier.height(12.dp))

            Text("Daily Totals (today)", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                TotalCard("Calories", latestTotals.calories.roundToInt().toString(), "kcal")
                TotalCard("Protein", latestTotals.protein.roundToInt().toString(), "g")
                TotalCard("Carbs", latestTotals.carbs.roundToInt().toString(), "g")
                TotalCard("Fat", latestTotals.fat.roundToInt().toString(), "g")
            }
            Spacer(modifier = Modifier.height(12.dp))

            Text("Progress vs Goals", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                ProgressCard("Calories", progress.calories)
                ProgressCard("Protein", progress.protein)
                ProgressCard("Carbs", progress.carbs)
                ProgressCard("Fat", progress.fat)
            }
            Spacer(modifier = Modifier.height(20.dp))

            Text("Set Goals", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                NumberField("Calories", cals, { cals = it })
                NumberField("Protein", prot, { prot = it })
                NumberField("Carbs", carb, { carb = it })
                NumberField("Fat", fati, { fati = it })
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = {
                    scope.launch {
                        nutritionViewModel.saveNutritionGoals(
                            userId = userId,
                            calories = cals.toDoubleOrNull() ?: 2000,
                            protein = prot.toDoubleOrNull() ?: 50,
                            carbs = carb.toDoubleOrNull() ?: 275,
                            fat = fati.toDoubleOrNull() ?: 65
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Save Goals") }
            Spacer(modifier = Modifier.height(20.dp))

            Text("Recent Logs", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            if (logs.isEmpty()) {
                Text("No nutrition logs yet.", style = MaterialTheme.typography.bodyMedium)
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(logs) { log ->
                        LogCard(log = log)
                    }
                }
            }
        }
    }
}

@Composable
private fun TotalCard(label: String, value: String, unit: String) {
    Card(modifier = Modifier.weight(1f)) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
            Text(value, style = MaterialTheme.typography.titleLarge)
            Text("$label $unit", style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
private fun ProgressCard(label: String, percent: Double) {
    val p = percent.coerceIn(0, 100)
    LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), progress = p / 100f)
    Text("$label ${p.roundToInt()}%", style = MaterialTheme.typography.labelSmall)
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

@Composable
private fun LogCard(log: NutritionLog) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("Logged ${log.date}", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Meals: ${log.meals.size} • Calories: ${log.totals.calories.roundToInt()} kcal", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
