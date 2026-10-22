package com.mealmanager.backend.controller

import com.mealmanager.backend.model.dto.ApiResponse
import com.mealmanager.backend.model.dto.NotificationDto
import com.mealmanager.backend.model.dto.NotificationPreferenceDto
import com.mealmanager.backend.service.NotificationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid
import java.util.UUID

@RestController
@RequestMapping("/api/v1/notifications")
class NotificationController(
    private val notificationService: NotificationService
) {

    @PostMapping
    fun sendNotification(@Valid @RequestBody dto: NotificationDto): ResponseEntity<ApiResponse<NotificationDto>> {
        val sent = notificationService.sendNotification(dto)
        return ResponseEntity.ok(ApiResponse.success(sent))
    }

    @PostMapping("/broadcast")
    fun broadcastNotification(@Valid @RequestBody request: BroadcastNotificationRequest): ResponseEntity<ApiResponse<Void>> {
        notificationService.broadcast(request)
        return ResponseEntity.ok(ApiResponse.success(null))
    }

    @GetMapping
    fun listNotifications(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<ApiResponse<Page<NotificationDto>>> {
        val result = notificationService.listNotifications(page, size)
        return ResponseEntity.ok(ApiResponse.success(result))
    }

    @PutMapping("/{id}/read")
    fun markRead(@PathVariable id: UUID): ResponseEntity<ApiResponse<Void>> {
        notificationService.markRead(id)
        return ResponseEntity.ok(ApiResponse.success(null))
    }

    @PutMapping("/preferences")
    fun updatePreferences(@Valid @RequestBody prefs: NotificationPreferenceDto): ResponseEntity<ApiResponse<NotificationPreferenceDto>> {
        val updated = notificationService.updatePreferences(prefs)
        return ResponseEntity.ok(ApiResponse.success(updated))
    }

    @GetMapping("/preferences")
    fun getPreferences(): ResponseEntity<ApiResponse<NotificationPreferenceDto>> {
        val prefs = notificationService.getPreferences()
        return ResponseEntity.ok(ApiResponse.success(prefs))
    }

    @PostMapping("/push/register")
    fun registerPushToken(@Valid @RequestBody request: PushTokenRequest): ResponseEntity<ApiResponse<Void>> {
        notificationService.registerPushToken(request)
        return ResponseEntity.ok(ApiResponse.success(null))
    }
}

data class BroadcastNotificationRequest(
    val title: String = "",
    val body: String = "",
    val userIds: List<String> = emptyList(),
    val topic: String? = null
)
data class PushTokenRequest(
    val userId: String = "",
    val token: String = "",
    val platform: String = "android"
)
