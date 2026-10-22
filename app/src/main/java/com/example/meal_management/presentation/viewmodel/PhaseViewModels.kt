package com.example.meal_management.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meal_management.data.model.*
import com.example.meal_management.data.repository.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class BarcodeViewModel(
    private val repository: BarcodeRepository = BarcodeRepository()
) : ViewModel() {
    private val _results = MutableStateFlow<List<BarcodeScanResult>>(emptyList())
    val results: StateFlow<List<BarcodeScanResult>> = _results.asStateFlow()
    private val _state = MutableStateFlow<PhaseState>(PhaseState.Initial)
    val state: StateFlow<PhaseState> = _state.asStateFlow()

    fun loadResults(userId: String) {
        viewModelScope.launch {
            _state.value = PhaseState.Loading
            repository.getScanResults(userId).collect { r ->
                when (r) { is Result.Success -> _results.value = r.data; is Result.Error -> _state.value = PhaseState.Error(r.exception.message ?: ""); else -> }
            }
        }
    }
    fun saveResult(result: BarcodeScanResult) {
        viewModelScope.launch {
            _state.value = PhaseState.Loading
            when (val r = repository.saveScanResult(result)) { is Result.Success -> _state.value = PhaseState.Success("Saved"); loadResults(result.id); is Result.Error -> _state.value = PhaseState.Error(r.exception.message ?: ""); else -> }
        }
    }
    fun loadPackagedFoods() {
        viewModelScope.launch {
            _state.value = PhaseState.Loading
            repository.getPackagedFoods().collect { r ->
                when (r) { is Result.Success -> /* packaged foods */; is Result.Error -> _state.value = PhaseState.Error(r.exception.message ?: ""); else -> }
            }
        }
    }
    fun reset() { _state.value = PhaseState.Initial }
}

class FridgeViewModel(
    private val repository: FridgeRepository = FridgeRepository()
) : ViewModel() {
    private val _items = MutableStateFlow<List<FridgeItem>>(emptyList())
    val items: StateFlow<List<FridgeItem>> = _items.asStateFlow()
    private val _state = MutableStateFlow<PhaseState>(PhaseState.Initial)
    val state: StateFlow<PhaseState> = _state.asStateFlow()

    fun loadItems(userId: String) {
        viewModelScope.launch {
            _state.value = PhaseState.Loading
            repository.getItems(userId).collect { r ->
                when (r) { is Result.Success -> _items.value = r.data; is Result.Error -> _state.value = PhaseState.Error(r.exception.message ?: ""); else -> }
            }
        }
    }
    fun addItem(item: FridgeItem) {
        viewModelScope.launch {
            _state.value = PhaseState.Loading
            when (val r = repository.addItem(item)) { is Result.Success -> _state.value = PhaseState.Success("Added"); loadItems(item.id); is Result.Error -> _state.value = PhaseState.Error(r.exception.message ?: ""); else -> }
        }
    }
    fun updateItem(item: FridgeItem) {
        viewModelScope.launch {
            when (val r = repository.updateItem(item)) { is Result.Success -> _state.value = PhaseState.Success("Updated"); loadItems(item.id); is Result.Error -> _state.value = PhaseState.Error(r.exception.message ?: ""); else -> }
        }
    }
    fun deleteItem(id: String, userId: String) {
        viewModelScope.launch {
            when (val r = repository.deleteItem(id)) { is Result.Success -> _state.value = PhaseState.Success("Deleted"); loadItems(userId); is Result.Error -> _state.value = PhaseState.Error(r.exception.message ?: ""); else -> }
        }
    }
    fun loadExpiring(userId: String, days: Int) {
        viewModelScope.launch {
            repository.getExpiringItems(userId, days).collect { r ->
                when (r) { is Result.Success -> _items.value = r.data; is Result.Error -> _state.value = PhaseState.Error(r.exception.message ?: ""); else -> }
            }
        }
    }
    fun expiringCount(items: List<FridgeItem>): Int = items.count { it.expirationDate > 0 && it.expirationDate - System.currentTimeMillis() <= 86400000 * 3 }
    fun reset() { _state.value = PhaseState.Initial }
}

class PantryViewModel(
    private val repository: PantryRepository = PantryRepository()
) : ViewModel() {
    private val _items = MutableStateFlow<List<PantryItem>>(emptyList())
    val items: StateFlow<List<PantryItem>> = _items.asStateFlow()
    private val _state = MutableStateFlow<PhaseState>(PhaseState.Initial)
    val state: StateFlow<PhaseState> = _state.asStateFlow()

    fun loadItems(userId: String) {
        viewModelScope.launch {
            _state.value = PhaseState.Loading
            repository.getItems(userId).collect { r ->
                when (r) { is Result.Success -> _items.value = r.data; is Result.Error -> _state.value = PhaseState.Error(r.exception.message ?: ""); else -> }
            }
        }
    }
    fun addItem(item: PantryItem) {
        viewModelScope.launch {
            _state.value = PhaseState.Loading
            when (val r = repository.addItem(item)) { is Result.Success -> _state.value = PhaseState.Success("Added"); loadItems(item.id); is Result.Error -> _state.value = PhaseState.Error(r.exception.message ?: ""); else -> }
        }
    }
    fun updateItem(item: PantryItem) {
        viewModelScope.launch {
            when (val r = repository.updateItem(item)) { is Result.Success -> _state.value = PhaseState.Success("Updated"); loadItems(item.id); is Result.Error -> _state.value = PhaseState.Error(r.exception.message ?: ""); else -> }
        }
    }
    fun belowThreshold(userId: String) {
        viewModelScope.launch {
            repository.getBelowThreshold(userId).collect { r ->
                when (r) { is Result.Success -> _items.value = r.data; is Result.Error -> _state.value = PhaseState.Error(r.exception.message ?: ""); else -> }
            }
        }
    }
    fun reset() { _state.value = PhaseState.Initial }
}

class WeightViewModel(
    private val repository: WeightRepository = WeightRepository()
) : ViewModel() {
    private val _logs = MutableStateFlow<List<WeightLog>>(emptyList())
    val logs: StateFlow<List<WeightLog>> = _logs.asStateFlow()
    private val _state = MutableStateFlow<PhaseState>(PhaseState.Initial)
    val state: StateFlow<PhaseState> = _state.asStateFlow()

    fun loadLogs(userId: String) {
        viewModelScope.launch {
            _state.value = PhaseState.Loading
            repository.getLogs(userId).collect { r ->
                when (r) { is Result.Success -> _logs.value = r.data; is Result.Error -> _state.value = PhaseState.Error(r.exception.message ?: ""); else -> }
            }
        }
    }
    fun addLog(log: WeightLog) {
        viewModelScope.launch {
            _state.value = PhaseState.Loading
            when (val r = repository.addLog(log)) { is Result.Success -> _state.value = PhaseState.Success("Added"); loadLogs(log.userId); is Result.Error -> _state.value = PhaseState.Error(r.exception.message ?: ""); else -> }
        }
    }
    fun latestWeight(): Double? = _logs.value.maxByOrNull { it.date }?.weightKg
    fun reset() { _state.value = PhaseState.Initial }
}

class WaterViewModel(
    private val repository: WaterRepository = WaterRepository()
) : ViewModel() {
    private val _logs = MutableStateFlow<List<WaterLog>>(emptyList())
    val logs: StateFlow<List<WaterLog>> = _logs.asStateFlow()
    private val _state = MutableStateFlow<PhaseState>(PhaseState.Initial)
    val state: StateFlow<PhaseState> = _state.asStateFlow()

    fun loadLogs(userId: String) {
        viewModelScope.launch {
            _state.value = PhaseState.Loading
            repository.getLogs(userId).collect { r ->
                when (r) { is Result.Success -> _logs.value = r.data; is Result.Error -> _state.value = PhaseState.Error(r.exception.message ?: ""); else -> }
            }
        }
    }
    fun addLog(log: WaterLog) {
        viewModelScope.launch {
            _state.value = PhaseState.Loading
            when (val r = repository.addLog(log)) { is Result.Success -> _state.value = PhaseState.Success("Added"); loadLogs(log.userId); is Result.Error -> _state.value = PhaseState.Error(r.exception.message ?: ""); else -> }
        }
    }
    fun dailyTotal(date: Long): Double = _logs.value.filter { it.date == date }.sumOf { it.amountMl }
    fun reset() { _state.value = PhaseState.Initial }
}

class FastingViewModel(
    private val repository: FastingRepository = FastingRepository()
) : ViewModel() {
    private val _windows = MutableStateFlow<List<FastingWindow>>(emptyList())
    val windows: StateFlow<List<FastingWindow>> = _windows.asStateFlow()
    private val _state = MutableStateFlow<PhaseState>(PhaseState.Initial)
    val state: StateFlow<PhaseState> = _state.asStateFlow()

    fun loadWindows(userId: String) {
        viewModelScope.launch {
            _state.value = PhaseState.Loading
            repository.getWindows(userId).collect { r ->
                when (r) { is Result.Success -> _windows.value = r.data; is Result.Error -> _state.value = PhaseState.Error(r.exception.message ?: ""); else -> }
            }
        }
    }
    fun addWindow(window: FastingWindow) {
        viewModelScope.launch {
            _state.value = PhaseState.Loading
            when (val r = repository.addWindow(window)) { is Result.Success -> _state.value = PhaseState.Success("Added"); loadWindows(window.userId); is Result.Error -> _state.value = PhaseState.Error(r.exception.message ?: ""); else -> }
        }
    }
    fun reset() { _state.value = PhaseState.Initial }
}

class MealPhotoViewModel(
    private val repository: MealPhotoRepository = MealPhotoRepository()
) : ViewModel() {
    private val _photos = MutableStateFlow<List<MealPhoto>>(emptyList())
    val photos: StateFlow<List<MealPhoto>> = _photos.asStateFlow()
    private val _state = MutableStateFlow<PhaseState>(PhaseState.Initial)
    val state: StateFlow<PhaseState> = _state.asStateFlow()

    fun loadPhotos(userId: String) {
        viewModelScope.launch {
            _state.value = PhaseState.Loading
            repository.getPhotos(userId).collect { r ->
                when (r) { is Result.Success -> _photos.value = r.data; is Result.Error -> _state.value = PhaseState.Error(r.exception.message ?: ""); else -> }
            }
        }
    }
    fun addPhoto(photo: MealPhoto) {
        viewModelScope.launch {
            _state.value = PhaseState.Loading
            when (val r = repository.addPhoto(photo)) { is Result.Success -> _state.value = PhaseState.Success("Added"); loadPhotos(photo.userId); is Result.Error -> _state.value = PhaseState.Error(r.exception.message ?: ""); else -> }
        }
    }
    fun reset() { _state.value = PhaseState.Initial }
}

class AuditLogViewModel(
    private val repository: AuditLogRepository = AuditLogRepository()
) : ViewModel() {
    private val _logs = MutableStateFlow<List<AuditLog>>(emptyList())
    val logs: StateFlow<List<AuditLog>> = _logs.asStateFlow()
    private val _state = MutableStateFlow<PhaseState>(PhaseState.Initial)
    val state: StateFlow<PhaseState> = _state.asStateFlow()

    fun loadLogs(userId: String) {
        viewModelScope.launch {
            _state.value = PhaseState.Loading
            repository.getLogs(userId).collect { r ->
                when (r) { is Result.Success -> _logs.value = r.data; is Result.Error -> _state.value = PhaseState.Error(r.exception.message ?: ""); else -> }
            }
        }
    }
    fun logAction(log: AuditLog) {
        viewModelScope.launch {
            when (val r = repository.logAction(log)) { is Result.Success -> _state.value = PhaseState.Success("Logged"); loadLogs(log.userId); is Result.Error -> _state.value = PhaseState.Error(r.exception.message ?: ""); else -> }
        }
    }
    fun reset() { _state.value = PhaseState.Initial }
}

class FeatureFlagViewModel(
    private val repository: FeatureFlagRepository = FeatureFlagRepository()
) : ViewModel() {
    private val _flags = MutableStateFlow<List<FeatureFlag>>(emptyList())
    val flags: StateFlow<List<FeatureFlag>> = _flags.asStateFlow()
    private val _state = MutableStateFlow<PhaseState>(PhaseState.Initial)
    val state: StateFlow<PhaseState> = _state.asStateFlow()

    fun loadFlags() {
        viewModelScope.launch {
            _state.value = PhaseState.Loading
            repository.getFlags().collect { r ->
                when (r) { is Result.Success -> _flags.value = r.data; is Result.Error -> _state.value = PhaseState.Error(r.exception.message ?: ""); else -> }
            }
        }
    }
    fun setFlag(flag: FeatureFlag) {
        viewModelScope.launch {
            _state.value = PhaseState.Loading
            when (val r = repository.setFlag(flag)) { is Result.Success -> _state.value = PhaseState.Success("Updated"); loadFlags(); is Result.Error -> _state.value = PhaseState.Error(r.exception.message ?: ""); else -> }
        }
    }
    fun isEnabled(key: String): Boolean = _flags.value.firstOrNull { it.key == key }?.enabled ?: false
    fun reset() { _state.value = PhaseState.Initial }
}

class AbTestViewModel(
    private val repository: AbTestRepository = AbTestRepository()
) : ViewModel() {
    private val _experiments = MutableStateFlow<List<AbTestExperiment>>(emptyList())
    val experiments: StateFlow<List<AbTestExperiment>> = _experiments.asStateFlow()
    private val _state = MutableStateFlow<PhaseState>(PhaseState.Initial)
    val state: StateFlow<PhaseState> = _state.asStateFlow()

    fun loadExperiments() {
        viewModelScope.launch {
            _state.value = PhaseState.Loading
            repository.getExperiments().collect { r ->
                when (r) { is Result.Success -> _experiments.value = r.data; is Result.Error -> _state.value = PhaseState.Error(r.exception.message ?: ""); else -> }
            }
        }
    }
    fun assignVariant(assignment: AbTestAssignment) {
        viewModelScope.launch {
            _state.value = PhaseState.Loading
            when (val r = repository.assignVariant(assignment)) { is Result.Success -> _state.value = PhaseState.Success("Assigned"); is Result.Error -> _state.value = PhaseState.Error(r.exception.message ?: ""); else -> }
        }
    }
    fun recordResult(result: AbTestResult) {
        viewModelScope.launch {
            when (val r = repository.recordResult(result)) { is Result.Success -> _state.value = PhaseState.Success("Recorded"); is Result.Error -> _state.value = PhaseState.Error(r.exception.message ?: ""); else -> }
        }
    }
    fun reset() { _state.value = PhaseState.Initial }
}

sealed class PhaseState {
    object Initial : PhaseState()
    object Loading : PhaseState()
    data class Success(val message: String) : PhaseState()
    data class Error(val message: String) : PhaseState()
}
