package com.mealmanager.backend.service

import com.mealmanager.backend.model.dto.Page
import com.mealmanager.backend.model.dto.WebhookEndpointDto
import com.mealmanager.backend.model.dto.WebhookDeliveryDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional
class WebhookService {

    fun createEndpoint(dto: WebhookEndpointDto): WebhookEndpointDto {
        return dto.copy(id = UUID.randomUUID())
    }

    fun listEndpoints(): List<WebhookEndpointDto> {
        return emptyList()
    }

    fun deleteEndpoint(id: UUID) {}

    fun deliver(id: UUID): WebhookDeliveryDto {
        return WebhookDeliveryDto(
            id = UUID.randomUUID(),
            endpointId = id,
            status = "delivered",
            responseCode = 200
        )
    }

    fun listDeliveries(page: Int, size: Int): Page<WebhookDeliveryDto> {
        return Page(content = emptyList(), page = page, size = size, total = 0)
    }

    fun ingest(endpointId: UUID, payload: String) {
        // Ingest webhook payload
    }
}
