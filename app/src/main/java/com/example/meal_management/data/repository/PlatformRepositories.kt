package com.example.meal_management.data.repository

import com.example.meal_management.data.model.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class PaymentRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    fun getTransactions(userId: String): Flow<Result<List<PaymentTransaction>>> = callbackFlow {
        val listener = firestore.collection("payments").whereEqualTo("userId", userId).addSnapshotListener { snapshot, error ->
            if (error != null) { trySend(Result.Error(error)); return@addSnapshotListener }
            if (snapshot != null) {
                trySend(Result.Success(snapshot.documents.mapNotNull { it.toObject(PaymentTransaction::class.java)?.copy(id = it.id) }))
            } else { trySend(Result.Success(emptyList())) }
        }
        awaitClose { listener.remove() }
    }
    suspend fun addTransaction(tx: PaymentTransaction): Result<String> = try {
        val ref = firestore.collection("payments").document()
        firestore.collection("payments").document(ref.id).set(tx).await()
        Result.Success(ref.id)
    } catch (e: Exception) { Result.Error(e) }
}

class DeliveryRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    fun getOrders(userId: String): Flow<Result<List<DeliveryOrder>>> = callbackFlow {
        val listener = firestore.collection("deliveries").whereEqualTo("userId", userId).addSnapshotListener { snapshot, error ->
            if (error != null) { trySend(Result.Error(error)); return@addSnapshotListener }
            if (snapshot != null) {
                trySend(Result.Success(snapshot.documents.mapNotNull { it.toObject(DeliveryOrder::class.java)?.copy(id = it.id) }))
            } else { trySend(Result.Success(emptyList())) }
        }
        awaitClose { listener.remove() }
    }
    suspend fun addOrder(order: DeliveryOrder): Result<String> = try {
        val ref = firestore.collection("deliveries").document()
        firestore.collection("deliveries").document(ref.id).set(order).await()
        Result.Success(ref.id)
    } catch (e: Exception) { Result.Error(e) }
}

class CalendarRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    fun getEvents(userId: String): Flow<Result<List<CalendarEvent>>> = callbackFlow {
        val listener = firestore.collection("calendarEvents").whereEqualTo("userId", userId).addSnapshotListener { snapshot, error ->
            if (error != null) { trySend(Result.Error(error)); return@addSnapshotListener }
            if (snapshot != null) {
                trySend(Result.Success(snapshot.documents.mapNotNull { it.toObject(CalendarEvent::class.java)?.copy(id = it.id) }))
            } else { trySend(Result.Success(emptyList())) }
        }
        awaitClose { listener.remove() }
    }
    suspend fun addEvent(event: CalendarEvent): Result<String> = try {
        val ref = firestore.collection("calendarEvents").document()
        firestore.collection("calendarEvents").document(ref.id).set(event).await()
        Result.Success(ref.id)
    } catch (e: Exception) { Result.Error(e) }
}

class NotificationRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    fun getNotifications(userId: String): Flow<Result<List<NotificationItem>>> = callbackFlow {
        val listener = firestore.collection("notifications").whereEqualTo("userId", userId).addSnapshotListener { snapshot, error ->
            if (error != null) { trySend(Result.Error(error)); return@addSnapshotListener }
            if (snapshot != null) {
                trySend(Result.Success(snapshot.documents.mapNotNull { it.toObject(NotificationItem::class.java)?.copy(id = it.id) }))
            } else { trySend(Result.Success(emptyList())) }
        }
        awaitClose { listener.remove() }
    }
    suspend fun addNotification(n: NotificationItem): Result<String> = try {
        val ref = firestore.collection("notifications").document()
        firestore.collection("notifications").document(ref.id).set(n).await()
        Result.Success(ref.id)
    } catch (e: Exception) { Result.Error(e) }
    suspend fun markRead(id: String): Result<Unit> = try {
        firestore.collection("notifications").document(id).update("isRead", true).await()
        Result.Success(Unit)
    } catch (e: Exception) { Result.Error(e) }
}

class LoyaltyRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    fun getAccount(userId: String): Flow<Result<LoyaltyAccount>> = callbackFlow {
        val listener = firestore.collection("loyaltyAccounts").whereEqualTo("userId", userId).limit(1).addSnapshotListener { snapshot, error ->
            if (error != null) { trySend(Result.Error(error)); return@addSnapshotListener }
            if (snapshot != null && snapshot.documents.isNotEmpty()) {
                val doc = snapshot.documents[0]
                val acc = doc.toObject(LoyaltyAccount::class.java)?.copy(id = doc.id)
                trySend(Result.Success(acc ?: LoyaltyAccount(userId = userId)))
            } else { trySend(Result.Success(LoyaltyAccount(userId = userId))) }
        }
        awaitClose { listener.remove() }
    }
    suspend fun saveAccount(account: LoyaltyAccount): Result<Unit> = try {
        firestore.collection("loyaltyAccounts").document(account.userId).set(account).await()
        Result.Success(Unit)
    } catch (e: Exception) { Result.Error(e) }
}

class SubscriptionRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    fun getSubscription(userId: String): Flow<Result<Subscription>> = callbackFlow {
        val listener = firestore.collection("subscriptions").whereEqualTo("userId", userId).limit(1).addSnapshotListener { snapshot, error ->
            if (error != null) { trySend(Result.Error(error)); return@addSnapshotListener }
            if (snapshot != null && snapshot.documents.isNotEmpty()) {
                val doc = snapshot.documents[0]
                val sub = doc.toObject(Subscription::class.java)?.copy(id = doc.id)
                trySend(Result.Success(sub ?: Subscription(userId = userId)))
            } else { trySend(Result.Success(Subscription(userId = userId))) }
        }
        awaitClose { listener.remove() }
    }
}
