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
import com.example.meal_management.data.model.*
import com.example.meal_management.presentation.viewmodel.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarcodeScreen(navController: NavController, viewModel: BarcodeViewModel = viewModel()) {
    val results by viewModel.results.collectAsState()
    val state by viewModel.state.collectAsState()
    var barcode by remember { mutableStateOf("") }
    Scaffold(topBar = { TopAppBar(title = { Text("Barcode Scanner") }) }) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Scan packaged food barcode", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(value = barcode, onValueChange = { barcode = it }, label = { Text("Barcode") }, singleLine = true, keyboardType = KeyboardType.Number, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = { viewModel.saveResult(BarcodeScanResult(barcode = barcode, found = true, foodName = "Scanned Item", nutrition = NutritionInfo(calories = 100.0))) }, modifier = Modifier.fillMaxWidth()) { Text("Log Scan") }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Recent Scans", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            if (results.isEmpty()) Text("No scans yet.", style = MaterialTheme.typography.bodyMedium)
            else LazyColumn { items(results) { r -> Text("${r.barcode} - ${r.foodName ?: "Unknown"}", style = MaterialTheme.typography.bodyMedium) } }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FridgeScreen(navController: NavController, viewModel: FridgeViewModel = viewModel()) {
    val items by viewModel.items.collectAsState()
    val state by viewModel.state.collectAsState()
    var name by remember { mutableStateOf("") }
    var qty by remember { mutableStateOf("1") }
    Scaffold(topBar = { TopAppBar(title = { Text("Fridge Inventory") }) }, floatingActionButton = { FloatingActionButton(onClick = { viewModel.loadExpiring("user", 7) }) { Icon(Icons.Default.Warning, contentDescription = "Expiring") } }) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Fridge Items", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Item name") }, singleLine = true, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = qty, onValueChange = { qty = it }, label = { Text("Quantity") }, singleLine = true, keyboardType = KeyboardType.Number, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = { viewModel.addItem(FridgeItem(name = name, quantity = qty.toDoubleOrNull() ?: 0.0)) }, modifier = Modifier.fillMaxWidth()) { Text("Add Item") }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Expiring soon: ${viewModel.expiringCount(items)}", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            if (items.isEmpty()) Text("No fridge items.", style = MaterialTheme.typography.bodyMedium)
            else LazyColumn { items(items) { it -> Text("${it.name} (${it.quantity} ${it.unit})", style = MaterialTheme.typography.bodyMedium) } }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantryScreen(navController: NavController, viewModel: PantryViewModel = viewModel()) {
    val items by viewModel.items.collectAsState()
    val state by viewModel.state.collectAsState()
    var name by remember { mutableStateOf("") }
    Scaffold(topBar = { TopAppBar(title = { Text("Pantry") }) }) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Pantry Items", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Staple item") }, singleLine = true, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = { viewModel.addItem(PantryItem(name = name, quantity = 1.0, reorderThreshold = 2)) }, modifier = Modifier.fillMaxWidth()) { Text("Add to Pantry") }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { viewModel.belowThreshold("user") }, modifier = Modifier.fillMaxWidth()) { Text("Show Low Stock") }
            Spacer(modifier = Modifier.height(12.dp))
            if (items.isEmpty()) Text("No pantry items.", style = MaterialTheme.typography.bodyMedium)
            else LazyColumn { items(items) { it -> Text("${it.name} (${it.quantity} ${it.unit})", style = MaterialTheme.typography.bodyMedium) } }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeightScreen(navController: NavController, viewModel: WeightViewModel = viewModel()) {
    val logs by viewModel.logs.collectAsState()
    val state by viewModel.state.collectAsState()
    var weight by remember { mutableStateOf("") }
    Scaffold(topBar = { TopAppBar(title = { Text("Weight Tracking") }) }) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Weight Logs", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(value = weight, onValueChange = { weight = it }, label = { Text("Weight (kg)") }, singleLine = true, keyboardType = KeyboardType.Number, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = { viewModel.addLog(WeightLog(userId = "user", weightKg = weight.toDoubleOrNull() ?: 0.0)) }, modifier = Modifier.fillMaxWidth()) { Text("Log Weight") }
            Spacer(modifier = Modifier.height(16.dp))
            val latest = viewModel.latestWeight()
            Text("Latest: ${latest?.toString() ?: "None"} kg", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            if (logs.isEmpty()) Text("No weight logs.", style = MaterialTheme.typography.bodyMedium)
            else LazyColumn { items(logs) { it -> Text("${it.date}: ${it.weightKg} kg", style = MaterialTheme.typography.bodyMedium) } }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaterScreen(navController: NavController, viewModel: WaterViewModel = viewModel()) {
    val logs by viewModel.logs.collectAsState()
    val state by viewModel.state.collectAsState()
    var amount by remember { mutableStateOf("250") }
    Scaffold(topBar = { TopAppBar(title = { Text("Water Tracking") }) }) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Water Intake", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(value = amount, onValueChange = { amount = it }, label = { Text("Amount (ml)") }, singleLine = true, keyboardType = KeyboardType.Number, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = { viewModel.addLog(WaterLog(userId = "user", date = System.currentTimeMillis(), amountMl = amount.toDoubleOrNull() ?: 0.0)) }, modifier = Modifier.fillMaxWidth()) { Text("Log Water") }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Today: ${viewModel.dailyTotal(System.currentTimeMillis()).roundToInt()} ml", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            if (logs.isEmpty()) Text("No water logs.", style = MaterialTheme.typography.bodyMedium)
            else LazyColumn { items(logs) { it -> Text("${it.date}: ${it.amountMl} ml", style = MaterialTheme.typography.bodyMedium) } }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FastingScreen(navController: NavController, viewModel: FastingViewModel = viewModel()) {
    val windows by viewModel.windows.collectAsState()
    val state by viewModel.state.collectAsState()
    Scaffold(topBar = { TopAppBar(title = { Text("Intermittent Fasting") }) }) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Fasting Windows", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = { viewModel.addWindow(FastingWindow(userId = "user", startTime = System.currentTimeMillis())) }, modifier = Modifier.fillMaxWidth()) { Text("Start Fasting") }
            Spacer(modifier = Modifier.height(16.dp))
            if (windows.isEmpty()) Text("No fasting windows.", style = MaterialTheme.typography.bodyMedium)
            else LazyColumn { items(windows) { it -> Text("Started ${it.startTime} - ${it.durationHours} hrs", style = MaterialTheme.typography.bodyMedium) } }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealPhotoScreen(navController: NavController, viewModel: MealPhotoViewModel = viewModel()) {
    val photos by viewModel.photos.collectAsState()
    val state by viewModel.state.collectAsState()
    Scaffold(topBar = { TopAppBar(title = { Text("Meal Photo Log") }) }) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Photo-Based Meal Logging", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = { /* launch camera */ }, modifier = Modifier.fillMaxWidth()) { Text("Take Photo") }
            Spacer(modifier = Modifier.height(16.dp))
            if (photos.isEmpty()) Text("No meal photos.", style = MaterialTheme.typography.bodyMedium)
            else LazyColumn { items(photos) { it -> Text(it.recognizedFood ?: "Meal", style = MaterialTheme.typography.bodyMedium) } }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuditLogScreen(navController: NavController, viewModel: AuditLogViewModel = viewModel()) {
    val logs by viewModel.logs.collectAsState()
    val state by viewModel.state.collectAsState()
    Scaffold(topBar = { TopAppBar(title = { Text("Audit Log") }) }) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Audit Trail", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(12.dp))
            if (logs.isEmpty()) Text("No audit logs.", style = MaterialTheme.typography.bodyMedium)
            else LazyColumn { items(logs) { it -> Text("${it.action}: ${it.details}", style = MaterialTheme.typography.bodyMedium) } }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeatureFlagScreen(navController: NavController, viewModel: FeatureFlagViewModel = viewModel()) {
    val flags by viewModel.flags.collectAsState()
    val state by viewModel.state.collectAsState()
    var key by remember { mutableStateOf("") }
    var enabled by remember { mutableStateOf(false) }
    Scaffold(topBar = { TopAppBar(title = { Text("Feature Flags") }) }) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Feature Flags", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(value = key, onValueChange = { key = it }, label = { Text("Flag key") }, singleLine = true, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(12.dp))
            Checkbox(checked = enabled, onChecked = { enabled = it })
            Text("Enabled", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = { viewModel.setFlag(FeatureFlag(key = key, enabled = enabled)) }, modifier = Modifier.fillMaxWidth()) { Text("Set Flag") }
            Spacer(modifier = Modifier.height(16.dp))
            if (flags.isEmpty()) Text("No flags.", style = MaterialTheme.typography.bodyMedium)
            else LazyColumn { items(flags) { f -> Row(modifier = Modifier.fillMaxWidth()) { Text(f.key, style = MaterialTheme.typography.bodyMedium); Spacer(modifier = Modifier.weight(1f)); Text("${f.enabled}", style = MaterialTheme.typography.bodyMedium) } } }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AbTestScreen(navController: NavController, viewModel: AbTestViewModel = viewModel()) {
    val experiments by viewModel.experiments.collectAsState()
    val state by viewModel.state.collectAsState()
    Scaffold(topBar = { TopAppBar(title = { Text("A/B Testing") }) }) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Experiments", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(12.dp))
            if (experiments.isEmpty()) Text("No experiments.", style = MaterialTheme.typography.bodyMedium)
            else LazyColumn { items(experiments) { e -> Text("${e.name} (${e.status})", style = MaterialTheme.typography.bodyMedium) } }
        }
    }
}
