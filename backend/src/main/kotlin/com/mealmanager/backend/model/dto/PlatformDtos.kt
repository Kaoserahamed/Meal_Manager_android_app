package com.mealmanager.backend.model.dto

import java.time.LocalDate
import java.util.UUID

data class PaymentDto(
    val id: UUID? = null,
    val amount: Double = 0.0,
    val currency: String = "USD",
    val status: String = "pending",
    val paymentMethodId: UUID? = null,
    val createdAt: String? = null
)

data class PaymentIntentDto(
    val amount: Double = 0.0,
    val currency: String = "USD",
    val paymentMethodId: UUID? = null,
    val description: String? = null
)

data class RefundDto(
    val amount: Double = 0.0,
    val currency: String = "USD",
    val reason: String? = null
)

data class DeliveryDto(
    val id: UUID? = null,
    val orderId: UUID? = null,
    val driverId: UUID? = null,
    val status: String = "pending",
    val address: String = "",
    val createdAt: String? = null
)

data class DeliveryAssignmentDto(
    val driverId: UUID = UUID.randomUUID()
)

data class CalendarEventDto(
    val id: UUID? = null,
    val title: String = "",
    val description: String? = null,
    val startDate: String = "",
    val endDate: String = "",
    val mealPlanId: UUID? = null
)

data class NotificationDto(
    val id: UUID? = null,
    val title: String = "",
    val body: String = "",
    val userId: String = "",
    val status: String = "pending",
    val createdAt: String? = null
)

data class NotificationPreferenceDto(
    val emailEnabled: Boolean = true,
    val pushEnabled: Boolean = true,
    val smsEnabled: Boolean = false
)

data class AnalyticsReportDto(
    val id: UUID? = null,
    val reportType: String = "",
    val generatedAt: String? = null
)

data class MetricsSummaryDto(
    val totalUsers: Long = 0,
    val activeUsers: Long = 0,
    val totalRecipes: Long = 0,
    val totalMealPlans: Long = 0,
    val revenue: Double = 0.0
)

data class MLRecommendationDto(
    val itemId: UUID = UUID.randomUUID(),
    val itemType: String = "",
    val score: Double = 0.0,
    val reason: String = ""
)

data class PersonalizationDto(
    val preferredCuisine: List<String> = emptyList(),
    val dietaryTags: List<String> = emptyList(),
    val spiceLevel: String = "medium"
)

data class OrganizationDto(
    val id: UUID? = null,
    val name: String = "",
    val plan: String = "free"
)

data class MembershipDto(
    val id: UUID? = null,
    val organizationId: UUID? = null,
    val userId: UUID? = null,
    val role: String = "member"
)

data class WebhookEndpointDto(
    val id: UUID? = null,
    val url: String = "",
    val secret: String? = null,
    val events: List<String> = emptyList()
)

data class WebhookDeliveryDto(
    val id: UUID? = null,
    val endpointId: UUID = UUID.randomUUID(),
    val status: String = "pending",
    val responseCode: Int = 0
)

data class SearchResultDto(
    val id: UUID = UUID.randomUUID(),
    val title: String = "",
    val type: String = "",
    val relevanceScore: Double = 0.0
)

data class CacheStatsDto(
    val hitRate: Double = 0.0,
    val totalEntries: Long = 0,
    val memoryUsedMB: Double = 0.0
)

data class RateLimitDto(
    val remaining: Int = 0,
    val resetAt: String = ""
)

data class AdminUserDto(
    val id: UUID = UUID.randomUUID(),
    val email: String = "",
    val role: String = "admin"
)

data class SystemHealthDto(
    val status: String = "",
    val database: String = "",
    val cache: String = ""
)

data class ModerationQueueDto(
    val id: UUID = UUID.randomUUID(),
    val contentType: String = "",
    val status: String = "pending"
)

data class ModerationActionDto(
    val contentId: UUID = UUID.randomUUID(),
    val action: String = "",
    val reason: String? = null
)

data class LoyaltyAccountDto(
    val userId: UUID = UUID.randomUUID(),
    val points: Int = 0,
    val tier: String = "bronze"
)

data class RewardDto(
    val id: UUID = UUID.randomUUID(),
    val name: String = "",
    val pointsCost: Int = 0
)

data class SubscriptionDto(
    val id: UUID? = null,
    val userId: UUID = UUID.randomUUID(),
    val plan: String = "free",
    val status: String = "active"
)

data class InvoiceDto(
    val id: UUID = UUID.randomUUID(),
    val subscriptionId: UUID = UUID.randomUUID(),
    val amount: Double = 0.0,
    val status: String = "paid"
)
