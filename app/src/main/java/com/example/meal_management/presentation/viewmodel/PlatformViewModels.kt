package com.example.meal_management.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meal_management.data.model.*
import com.example.meal_management.data.repository.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PaymentViewModel(
    private val repository: PaymentRepository = PaymentRepository()
) : ViewModel() {
    private val _transactions = MutableStateFlow<List<PaymentTransaction>>(emptyList())
    val transactions: StateFlow<List<PaymentTransaction>> = _transactions.asStateFlow()
    private val _state = MutableStateFlow<PhaseState>(PhaseState.Initial)
    val state: StateFlow<PhaseState> = _state.asStateFlow()

    fun loadTransactions(userId: String) {
        viewModelScope.launch {
            _state.value = PhaseState.Loading
            repository.getTransactions(userId).collect { r ->
                when (r) { is Result.Success -> _transactions.value = r.data; is Result.Error -> _state.value = PhaseState.Error(r.exception.message ?: ""); else -> }
            }
        }
    }
    fun addTransaction(tx: PaymentTransaction) {
        viewModelScope.launch {
            _state.value = PhaseState.Loading
            when (val r = repository.addTransaction(tx)) { is Result.Success -> _state.value = PhaseState.Success("Added"); loadTransactions(tx.userId); is Result.Error -> _state.value = PhaseState.Error(r.exception.message ?: ""); else -> }
        }
    }
    fun reset() { _state.value = PhaseState.Initial }
}

class DeliveryViewModel(
    private val repository: DeliveryRepository = DeliveryRepository()
) : ViewModel() {
    private val _orders = MutableStateFlow<List<DeliveryOrder>>(emptyList())
    val orders: StateFlow<List<DeliveryOrder>> = _orders.asStateFlow()
    private val _state = MutableStateFlow<PhaseState>(PhaseState.Initial)
    val state: StateFlow<PhaseState> = _state.asStateFlow()

    fun loadOrders(userId: String) {
        viewModelScope.launch {
            _state.value = PhaseState.Loading
            repository.getOrders(userId).collect { r ->
                when (r) { is Result.Success -> _orders.value = r.data; is Result.Error -> _state.value = PhaseState.Error(r.exception.message ?: ""); else -> }
            }
        }
    }
    fun addOrder(order: DeliveryOrder) {
        viewModelScope.launch {
            _state.value = PhaseState.Loading
            when (val r = repository.addOrder(order)) { is Result.Success -> _state.value = PhaseState.Success("Added"); loadOrders(order.userId); is Result.Error -> _state.value = PhaseState.Error(r.exception.message ?: ""); else -> }
        }
    }
    fun reset() { _state.value = PhaseState.Initial }
}

class CalendarViewModel(
    private val repository: CalendarRepository = CalendarRepository()
) : ViewModel() {
    private val _events = MutableStateFlow<List<CalendarEvent>>(emptyList())
    val events: StateFlow<List<CalendarEvent>> = _events.asStateFlow()
    private val _state = MutableStateFlow<PhaseState>(PhaseState.Initial)
    val state: StateFlow<PhaseState> = _state.asStateFlow()

    fun loadEvents(userId: String) {
        viewModelScope.launch {
            _state.value = PhaseState.Loading
            repository.getEvents(userId).collect { r ->
                when (r) { is Result.Success -> _events.value = r.data; is Result.Error -> _state.value = PhaseState.Error(r.exception.message ?: ""); else -> }
            }
        }
    }
    fun addEvent(event: CalendarEvent) {
        viewModelScope.launch {
            _state.value = PhaseState.Loading
            when (val r = repository.addEvent(event)) { is Result.Success -> _state.value = PhaseState.Success("Added"); loadEvents(event.userId); is Result.Error -> _state.value = PhaseState.Error(r.exception.message ?: ""); else -> }
        }
    }
    fun reset() { _state.value = PhaseState.Initial }
}

class NotificationViewModel(
    private val repository: NotificationRepository = NotificationRepository()
) : ViewModel() {
    private val _notifications = MutableStateFlow<List<NotificationItem>>(emptyList())
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()
    private val _state = MutableStateFlow<PhaseState>(PhaseState.Initial)
    val state: StateFlow<PhaseState> = _state.asStateFlow()

    fun loadNotifications(userId: String) {
        viewModelScope.launch {
            _state.value = PhaseState.Loading
            repository.getNotifications(userId).collect { r ->
                when (r) { is Result.Success -> _notifications.value = r.data; is Result.Error -> _state.value = PhaseState.Error(r.exception.message ?: ""); else -> }
            }
        }
    }
    fun addNotification(n: NotificationItem) {
        viewModelScope.launch {
            _state.value = PhaseState.Loading
            when (val r = repository.addNotification(n)) { is Result.Success -> _state.value = PhaseState.Success("Added"); loadNotifications(n.userId); is Result.Error -> _state.value = PhaseState.Error(r.exception.message ?: ""); else -> }
        }
    }
    fun markRead(id: String, userId: String) {
        viewModelScope.launch {
            when (val r = repository.markRead(id)) { is Result.Success -> _state.value = PhaseState.Success("Marked"); loadNotifications(userId); is Result.Error -> _state.value = PhaseState.Error(r.exception.message ?: ""); else -> }
        }
    }
    fun reset() { _state.value = PhaseState.Initial }
}

class LoyaltyViewModel(
    private val repository: LoyaltyRepository = LoyaltyRepository()
) : ViewModel() {
    private val _account = MutableStateFlow<LoyaltyAccount?>(null)
    val account: StateFlow<LoyaltyAccount?> = _account.asStateFlow()
    private val _state = MutableStateFlow<PhaseState>(PhaseState.Initial)
    val state: StateFlow<PhaseState> = _state.asStateFlow()

    fun loadAccount(userId: String) {
        viewModelScope.launch {
            _state.value = PhaseState.Loading
            repository.getAccount(userId).collect { r ->
                when (r) { is Result.Success -> _account.value = r.data; is Result.Error -> _state.value = PhaseState.Error(r.exception.message ?: ""); else -> }
            }
        }
    }
    fun saveAccount(account: LoyaltyAccount) {
        viewModelScope.launch {
            _state.value = PhaseState.Loading
            when (val r = repository.saveAccount(account)) { is Result.Success -> _state.value = PhaseState.Success("Saved"); loadAccount(account.userId); is Result.Error -> _state.value = PhaseState.Error(r.exception.message ?: ""); else -> }
        }
    }
    fun reset() { _state.value = PhaseState.Initial }
}

class SubscriptionViewModel(
    private val repository: SubscriptionRepository = SubscriptionRepository()
) : ViewModel() {
    private val _subscription = MutableStateFlow<Subscription?>(null)
    val subscription: StateFlow<Subscription?> = _subscription.asStateFlow()
    private val _state = MutableStateFlow<PhaseState>(PhaseState.Initial)
    val state: StateFlow<PhaseState> = _state.asStateFlow()

    fun loadSubscription(userId: String) {
        viewModelScope.launch {
            _state.value = PhaseState.Loading
            repository.getSubscription(userId).collect { r ->
                when (r) { is Result.Success -> _subscription.value = r.data; is Result.Error -> _state.value = PhaseState.Error(r.exception.message ?: ""); else -> }
            }
        }
    }
    fun reset() { _state.value = PhaseState.Initial }
}
