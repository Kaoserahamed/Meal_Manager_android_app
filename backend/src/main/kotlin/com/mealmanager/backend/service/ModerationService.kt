package com.mealmanager.backend.service

import com.mealmanager.backend.model.dto.Page
import com.mealmanager.backend.model.dto.ModerationQueueDto
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ModerationService {

    fun getQueue(page: Int, size: Int): Page<ModerationQueueDto> {
        return Page(content = emptyList(), page = page, size = size, total = 0)
    }

    fun takeAction(dto: com.mealmanager.backend.controller.ModerationActionDto) {
        // Take moderation action
    }

    fun getReview(id: UUID): ModerationQueueDto {
        return ModerationQueueDto(id = id, contentType = "recipe", status = "pending")
    }
}
