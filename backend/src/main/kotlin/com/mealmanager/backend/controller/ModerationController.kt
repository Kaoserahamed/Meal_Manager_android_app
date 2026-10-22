package com.mealmanager.backend.controller

import com.mealmanager.backend.model.dto.ApiResponse
import com.mealmanager.backend.model.dto.ModerationQueueDto
import com.mealmanager.backend.model.dto.ModerationActionDto
import com.mealmanager.backend.service.ModerationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import jakarta.validation.Valid
import java.util.UUID

@RestController
@RequestMapping("/api/v1/moderation")
class ModerationController(
    private val moderationService: ModerationService
) {

    @GetMapping("/queue")
    fun getQueue(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<ApiResponse<Page<ModerationQueueDto>>> {
        val result = moderationService.getQueue(page, size)
        return ResponseEntity.ok(ApiResponse.success(result))
    }

    @PostMapping("/actions")
    fun takeAction(@Valid @RequestBody dto: ModerationActionDto): ResponseEntity<ApiResponse<Void>> {
        moderationService.takeAction(dto)
        return ResponseEntity.ok(ApiResponse.success(null))
    }

    @GetMapping("/reviews/{id}")
    fun getReview(@PathVariable id: UUID): ResponseEntity<ApiResponse<ModerationQueueDto>> {
        val review = moderationService.getReview(id)
        return ResponseEntity.ok(ApiResponse.success(review))
    }
}
