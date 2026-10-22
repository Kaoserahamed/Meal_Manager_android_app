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
import com.example.meal_management.data.model.*
import com.example.meal_management.presentation.viewmodel.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(navController: NavController, viewModel: PaymentViewModel = viewModel()) {
    val transactions by viewModel.transactions.collectAsState()
    val state by viewModel.state.collectAsState()
    var amount by remember { mutableStateOf("") }
    Scaffold(topBar = { TopAppBar(title = { Text("Payments") }) }) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Payment Transactions", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(value = amount, onValueChange = { amount = it }, label = { Text("Amount") }, singleLine = true, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = { viewModel.addTransaction(PaymentTransaction(userId = "user", amount = amount.toDoubleOrNull() ?: 0.0)) }, modifier = Modifier.fillMaxWidth()) { Text("Add Transaction") }
            Spacer(modifier = Modifier.height(16.dp))
            if (transactions.isEmpty()) Text("No transactions.", style = MaterialTheme.typography.bodyMedium)
            else LazyColumn { items(transactions) { t -> Text("${t.amount} ${t.currency} - ${t.status}", style = MaterialTheme.typography.bodyMedium) } }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeliveryScreen(navController: NavController, viewModel: DeliveryViewModel = viewModel()) {
    val orders by viewModel.orders.collectAsState()
    val state by viewModel.state.collectAsState()
    var address by remember { mutableStateOf("") }
    Scaffold(topBar = { TopAppBar(title = { Text("Delivery") }) }) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Delivery Orders", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(value = address, onValueChange = { address = it }, label = { Text("Address") }, singleLine = true, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = { viewModel.addOrder(DeliveryOrder(userId = "user", address = address)) }, modifier = Modifier.fillMaxWidth()) { Text("Place Order") }
            Spacer(modifier = Modifier.height(16.dp))
            if (orders.isEmpty()) Text("No deliveries.", style = MaterialTheme.typography.bodyMedium)
            else LazyColumn { items(orders) { o -> Text("${o.status} - ${o.address}", style = MaterialTheme.typography.bodyMedium) } }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(navController: NavController, viewModel: CalendarViewModel = viewModel()) {
    val events by viewModel.events.collectAsState()
    val state by viewModel.state.collectAsState()
    var title by remember { mutableStateOf("") }
    Scaffold(topBar = { TopAppBar(title = { Text("Calendar") }) }) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Calendar Events", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Event title") }, singleLine = true, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = { viewModel.addEvent(CalendarEvent(userId = "user", title = title)) }, modifier = Modifier.fillMaxWidth()) { Text("Add Event") }
            Spacer(modifier = Modifier.height(16.dp))
            if (events.isEmpty()) Text("No events.", style = MaterialTheme.typography.bodyMedium)
            else LazyColumn { items(events) { e -> Text(e.title, style = MaterialTheme.typography.bodyMedium) } }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(navController: NavController, viewModel: NotificationViewModel = viewModel()) {
    val notifications by viewModel.notifications.collectAsState()
    val state by viewModel.state.collectAsState()
    Scaffold(topBar = { TopAppBar(title = { Text("Notifications") }) }) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Notifications", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(12.dp))
            if (notifications.isEmpty()) Text("No notifications.", style = MaterialTheme.typography.bodyMedium)
            else LazyColumn { items(notifications) { n ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column { Text(n.title, style = MaterialTheme.typography.bodyMedium); Text(n.body, style = MaterialTheme.typography.labelSmall) }
                    Spacer(modifier = Modifier.weight(1f))
                    Checkbox(checked = n.isRead, onChecked = { viewModel.markRead(n.id, "user") })
                }
            } }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoyaltyScreen(navController: NavController, viewModel: LoyaltyViewModel = viewModel()) {
    val account by viewModel.account.collectAsState()
    val state by viewModel.state.collectAsState()
    Scaffold(topBar = { TopAppBar(title = { Text("Loyalty") }) }) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Loyalty Account", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(12.dp))
            Text("Points: ${account?.points ?: 0}", style = MaterialTheme.typography.titleMedium)
            Text("Tier: ${account?.tier ?: "bronze"}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionScreen(navController: NavController, viewModel: SubscriptionViewModel = viewModel()) {
    val subscription by viewModel.subscription.collectAsState()
    val state by viewModel.state.collectAsState()
    Scaffold(topBar = { TopAppBar(title = { Text("Subscription") }) }) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Subscription", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(12.dp))
            Text("Plan: ${subscription?.plan ?: "free"}", style = MaterialTheme.typography.titleMedium)
            Text("Status: ${subscription?.status ?: "inactive"}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
