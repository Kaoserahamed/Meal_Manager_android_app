package com.example.meal_management.data.model

import com.google.firebase.firestore.DocumentId

data class PaymentTransaction(
    @DocumentId val id: String = "",
    val userId: String = "",
    val amount: Double = 0.0,
    val currency: String = "USD",
    val status: String = "pending",
    val paymentMethod: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

data class DeliveryOrder(
    @DocumentId val id: String = "",
    val userId: String = "",
    val driverId: String? = null,
    val status: String = "pending",
    val address: String = "",
    val estimatedDelivery: Long = 0L,
    val createdAt: Long = System.currentTimeMillis()
)

data class CalendarEvent(
    @DocumentId val id: String = "",
    val userId: String = "",
    val title: String = "",
    val description: String? = null,
    val startDate: Long = 0L,
    val endDate: Long = 0L,
    val mealPlanId: String? = null
)

data class NotificationItem(
    @DocumentId val id: String = "",
    val userId: String = "",
    val title: String = "",
    val body: String = "",
    val isRead: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

data class NotificationPreference(
    @DocumentId val id: String = "",
    val userId: String = "",
    val emailEnabled: Boolean = true,
    val pushEnabled: Boolean = true,
    val smsEnabled: Boolean = false
)

data class AnalyticsReport(
    @DocumentId val id: String = "",
    val userId: String = "",
    val reportType: String = "",
    val generatedAt: Long = System.currentTimeMillis()
)

data class LoyaltyAccount(
    @DocumentId val id: String = "",
    val userId: String = "",
    val points: Int = 0,
    val tier: String = "bronze"
)

data class Reward(
    @DocumentId val id: String = "",
    val name: String = "",
    val pointsCost: Int = 0
)

data class Subscription(
    @DocumentId val id: String = "",
    val userId: String = "",
    val plan: String = "free",
    val status: String = "active"
)

data class Invoice(
    @DocumentId val id: String = "",
    val userId: String = "",
    val amount: Double = 0.0,
    val status: String = "paid"
)

data class Organization(
    @DocumentId val id: String = "",
    val name: String = "",
    val plan: String = "free"
)

data class Membership(
    @DocumentId val id: String = "",
    val organizationId: String = "",
    val userId: String = "",
    val role: String = "member"
)

data class WebhookEndpoint(
    @DocumentId val id: String = "",
    val url: String = "",
    val events: List<String> = emptyList()
)

data class SearchHistory(
    @DocumentId val id: String = "",
    val userId: String = "",
    val query: String = "",
    val searchedAt: Long = System.currentTimeMillis()
)

data class CacheEntry(
    val key: String = "",
    val value: String = "",
    val expiresAt: Long = 0L
)

data class RateLimitInfo(
    val key: String = "",
    val remaining: Int = 0,
    val resetAt: Long = 0L
)

data class AdminAction(
    @DocumentId val id: String = "",
    val adminId: String = "",
    val action: String = "",
    val targetId: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

data class ModerationItem(
    @DocumentId val id: String = "",
    val contentType: String = "",
    val status: String = "pending"
)
