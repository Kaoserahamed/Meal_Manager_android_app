package com.example.meal_management.data.repository

import com.example.meal_management.data.model.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class BarcodeRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    fun getScanResults(userId: String): Flow<Result<List<BarcodeScanResult>>> = callbackFlow {
        val listener = firestore.collection("barcodeScans")
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) { trySend(Result.Error(error)); return@addSnapshotListener }
                if (snapshot != null) {
                    trySend(Result.Success(snapshot.documents.mapNotNull { it.toObject(BarcodeScanResult::class.java)?.copy(id = it.id) }))
                } else { trySend(Result.Success(emptyList())) }
            }
        awaitClose { listener.remove() }
    }
    suspend fun saveScanResult(result: BarcodeScanResult): Result<String> = try {
        val ref = firestore.collection("barcodeScans").document()
        firestore.collection("barcodeScans").document(ref.id).set(result).await()
        Result.Success(ref.id)
    } catch (e: Exception) { Result.Error(e) }
    fun getPackagedFoods(): Flow<Result<List<PackagedFood>>> = callbackFlow {
        val listener = firestore.collection("packagedFoods").addSnapshotListener { snapshot, error ->
            if (error != null) { trySend(Result.Error(error)); return@addSnapshotListener }
            if (snapshot != null) {
                trySend(Result.Success(snapshot.documents.mapNotNull { it.toObject(PackagedFood::class.java)?.copy(id = it.id) }))
            } else { trySend(Result.Success(emptyList())) }
        }
        awaitClose { listener.remove() }
    }
    suspend fun addPackagedFood(food: PackagedFood): Result<String> = try {
        val ref = firestore.collection("packagedFoods").document()
        firestore.collection("packagedFoods").document(ref.id).set(food).await()
        Result.Success(ref.id)
    } catch (e: Exception) { Result.Error(e) }
}

class FridgeRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    fun getItems(userId: String): Flow<Result<List<FridgeItem>>> = callbackFlow {
        val listener = firestore.collection("fridgeItems").whereEqualTo("userId", userId).addSnapshotListener { snapshot, error ->
            if (error != null) { trySend(Result.Error(error)); return@addSnapshotListener }
            if (snapshot != null) {
                trySend(Result.Success(snapshot.documents.mapNotNull { it.toObject(FridgeItem::class.java)?.copy(id = it.id) }))
            } else { trySend(Result.Success(emptyList())) }
        }
        awaitClose { listener.remove() }
    }
    suspend fun addItem(item: FridgeItem): Result<String> = try {
        val ref = firestore.collection("fridgeItems").document()
        firestore.collection("fridgeItems").document(ref.id).set(item).await()
        Result.Success(ref.id)
    } catch (e: Exception) { Result.Error(e) }
    suspend fun updateItem(item: FridgeItem): Result<Unit> = try {
        firestore.collection("fridgeItems").document(item.id).set(item).await(); Result.Success(Unit)
    } catch (e: Exception) { Result.Error(e) }
    suspend fun deleteItem(id: String): Result<Unit> = try {
        firestore.collection("fridgeItems").document(id).delete().await(); Result.Success(Unit)
    } catch (e: Exception) { Result.Error(e) }
    fun getExpiringItems(userId: String, days: Int): Flow<Result<List<FridgeItem>>> = callbackFlow {
        val cutoff = System.currentTimeMillis() + days * 86400000L
        val listener = firestore.collection("fridgeItems")
            .whereEqualTo("userId", userId)
            .whereLessThan("expirationDate", cutoff)
            .addSnapshotListener { snapshot, error ->
                if (error != null) { trySend(Result.Error(error)); return@addSnapshotListener }
                if (snapshot != null) {
                    trySend(Result.Success(snapshot.documents.mapNotNull { it.toObject(FridgeItem::class.java)?.copy(id = it.id) }))
                } else { trySend(Result.Success(emptyList())) }
            }
        awaitClose { listener.remove() }
    }
}

class PantryRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    fun getItems(userId: String): Flow<Result<List<PantryItem>>> = callbackFlow {
        val listener = firestore.collection("pantryItems").whereEqualTo("userId", userId).addSnapshotListener { snapshot, error ->
            if (error != null) { trySend(Result.Error(error)); return@addSnapshotListener }
            if (snapshot != null) {
                trySend(Result.Success(snapshot.documents.mapNotNull { it.toObject(PantryItem::class.java)?.copy(id = it.id) }))
            } else { trySend(Result.Success(emptyList())) }
        }
        awaitClose { listener.remove() }
    }
    suspend fun addItem(item: PantryItem): Result<String> = try {
        val ref = firestore.collection("pantryItems").document()
        firestore.collection("pantryItems").document(ref.id).set(item).await()
        Result.Success(ref.id)
    } catch (e: Exception) { Result.Error(e) }
    suspend fun updateItem(item: PantryItem): Result<Unit> = try {
        firestore.collection("pantryItems").document(item.id).set(item).await(); Result.Success(Unit)
    } catch (e: Exception) { Result.Error(e) }
    fun getBelowThreshold(userId: String): Flow<Result<List<PantryItem>>> = callbackFlow {
        val listener = firestore.collection("pantryItems").whereEqualTo("userId", userId).addSnapshotListener { snapshot, error ->
            if (error != null) { trySend(Result.Error(error)); return@addSnapshotListener }
            if (snapshot != null) {
                trySend(Result.Success(snapshot.documents.mapNotNull { doc ->
                    val item = doc.toObject(PantryItem::class.java)?.copy(id = doc.id)
                    item.takeIf { it.quantity < it.reorderThreshold }
                }))
            } else { trySend(Result.Success(emptyList())) }
        }
        awaitClose { listener.remove() }
    }
}

class WeightRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    fun getLogs(userId: String): Flow<Result<List<WeightLog>>> = callbackFlow {
        val listener = firestore.collection("weightLogs").whereEqualTo("userId", userId).addSnapshotListener { snapshot, error ->
            if (error != null) { trySend(Result.Error(error)); return@addSnapshotListener }
            if (snapshot != null) {
                trySend(Result.Success(snapshot.documents.mapNotNull { it.toObject(WeightLog::class.java)?.copy(id = it.id) }))
            } else { trySend(Result.Success(emptyList())) }
        }
        awaitClose { listener.remove() }
    }
    suspend fun addLog(log: WeightLog): Result<String> = try {
        val ref = firestore.collection("weightLogs").document()
        firestore.collection("weightLogs").document(ref.id).set(log).await()
        Result.Success(ref.id)
    } catch (e: Exception) { Result.Error(e) }
}

class WaterRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    fun getLogs(userId: String): Flow<Result<List<WaterLog>>> = callbackFlow {
        val listener = firestore.collection("waterLogs").whereEqualTo("userId", userId).addSnapshotListener { snapshot, error ->
            if (error != null) { trySend(Result.Error(error)); return@addSnapshotListener }
            if (snapshot != null) {
                trySend(Result.Success(snapshot.documents.mapNotNull { it.toObject(WaterLog::class.java)?.copy(id = it.id) }))
            } else { trySend(Result.Success(emptyList())) }
        }
        awaitClose { listener.remove() }
    }
    suspend fun addLog(log: WaterLog): Result<String> = try {
        val ref = firestore.collection("waterLogs").document()
        firestore.collection("waterLogs").document(ref.id).set(log).await()
        Result.Success(ref.id)
    } catch (e: Exception) { Result.Error(e) }
}

class FastingRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    fun getWindows(userId: String): Flow<Result<List<FastingWindow>>> = callbackFlow {
        val listener = firestore.collection("fastingWindows").whereEqualTo("userId", userId).addSnapshotListener { snapshot, error ->
            if (error != null) { trySend(Result.Error(error)); return@addSnapshotListener }
            if (snapshot != null) {
                trySend(Result.Success(snapshot.documents.mapNotNull { it.toObject(FastingWindow::class.java)?.copy(id = it.id) }))
            } else { trySend(Result.Success(emptyList())) }
        }
        awaitClose { listener.remove() }
    }
    suspend fun addWindow(window: FastingWindow): Result<String> = try {
        val ref = firestore.collection("fastingWindows").document()
        firestore.collection("fastingWindows").document(ref.id).set(window).await()
        Result.Success(ref.id)
    } catch (e: Exception) { Result.Error(e) }
}

class MealPhotoRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    fun getPhotos(userId: String): Flow<Result<List<MealPhoto>>> = callbackFlow {
        val listener = firestore.collection("mealPhotos").whereEqualTo("userId", userId).addSnapshotListener { snapshot, error ->
            if (error != null) { trySend(Result.Error(error)); return@addSnapshotListener }
            if (snapshot != null) {
                trySend(Result.Success(snapshot.documents.mapNotNull { it.toObject(MealPhoto::class.java)?.copy(id = it.id) }))
            } else { trySend(Result.Success(emptyList())) }
        }
        awaitClose { listener.remove() }
    }
    suspend fun addPhoto(photo: MealPhoto): Result<String> = try {
        val ref = firestore.collection("mealPhotos").document()
        firestore.collection("mealPhotos").document(ref.id).set(photo).await()
        Result.Success(ref.id)
    } catch (e: Exception) { Result.Error(e) }
}

class AuditLogRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    fun getLogs(userId: String): Flow<Result<List<AuditLog>>> = callbackFlow {
        val listener = firestore.collection("auditLogs").whereEqualTo("userId", userId).addSnapshotListener { snapshot, error ->
            if (error != null) { trySend(Result.Error(error)); return@addSnapshotListener }
            if (snapshot != null) {
                trySend(Result.Success(snapshot.documents.mapNotNull { it.toObject(AuditLog::class.java)?.copy(id = it.id) }))
            } else { trySend(Result.Success(emptyList())) }
        }
        awaitClose { listener.remove() }
    }
    suspend fun logAction(log: AuditLog): Result<String> = try {
        val ref = firestore.collection("auditLogs").document()
        firestore.collection("auditLogs").document(ref.id).set(log).await()
        Result.Success(ref.id)
    } catch (e: Exception) { Result.Error(e) }
}

class FeatureFlagRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    fun getFlags(): Flow<Result<List<FeatureFlag>>> = callbackFlow {
        val listener = firestore.collection("featureFlags").addSnapshotListener { snapshot, error ->
            if (error != null) { trySend(Result.Error(error)); return@addSnapshotListener }
            if (snapshot != null) {
                trySend(Result.Success(snapshot.documents.mapNotNull { it.toObject(FeatureFlag::class.java)?.copy(id = it.id) }))
            } else { trySend(Result.Success(emptyList())) }
        }
        awaitClose { listener.remove() }
    }
    suspend fun setFlag(flag: FeatureFlag): Result<Unit> = try {
        firestore.collection("featureFlags").document(flag.key).set(flag).await(); Result.Success(Unit)
    } catch (e: Exception) { Result.Error(e) }
    fun isEnabled(key: String): Flow<Result<Boolean>> = callbackFlow {
        val listener = firestore.collection("featureFlags").document(key).addSnapshotListener { snapshot, error ->
            if (error != null) { trySend(Result.Error(error)); return@addSnapshotListener }
            if (snapshot != null && snapshot.exists()) {
                val flag = snapshot.toObject(FeatureFlag::class.java)
                trySend(Result.Success(flag?.enabled ?: false))
            } else { trySend(Result.Success(false)) }
        }
        awaitClose { listener.remove() }
    }
}

class AbTestRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    fun getExperiments(): Flow<Result<List<AbTestExperiment>>> = callbackFlow {
        val listener = firestore.collection("abTestExperiments").addSnapshotListener { snapshot, error ->
            if (error != null) { trySend(Result.Error(error)); return@addSnapshotListener }
            if (snapshot != null) {
                trySend(Result.Success(snapshot.documents.mapNotNull { it.toObject(AbTestExperiment::class.java)?.copy(id = it.id) }))
            } else { trySend(Result.Success(emptyList())) }
        }
        awaitClose { listener.remove() }
    }
    suspend fun assignVariant(assignment: AbTestAssignment): Result<String> = try {
        val ref = firestore.collection("abTestAssignments").document()
        firestore.collection("abTestAssignments").document(ref.id).set(assignment).await()
        Result.Success(ref.id)
    } catch (e: Exception) { Result.Error(e) }
    suspend fun recordResult(result: AbTestResult): Result<String> = try {
        val ref = firestore.collection("abTestResults").document()
        firestore.collection("abTestResults").document(ref.id).set(result).await()
        Result.Success(ref.id)
    } catch (e: Exception) { Result.Error(e) }
}
