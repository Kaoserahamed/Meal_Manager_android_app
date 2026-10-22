package com.mealmanager.backend.controller

import com.mealmanager.backend.model.dto.ApiResponse
import com.mealmanager.backend.model.dto.CalendarEventDto
import com.mealmanager.backend.service.CalendarService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid
import java.time.LocalDate
import java.util.UUID

@RestController
@RequestMapping("/api/v1/calendar")
class CalendarController(
    private val calendarService: CalendarService
) {

    @GetMapping
    fun listEvents(
        @RequestParam startDate: LocalDate,
        @RequestParam endDate: LocalDate
    ): ResponseEntity<ApiResponse<List<CalendarEventDto>>> {
        val events = calendarService.listEvents(startDate, endDate)
        return ResponseEntity.ok(ApiResponse.success(events))
    }

    @PostMapping
    fun createEvent(@Valid @RequestBody dto: CalendarEventDto): ResponseEntity<ApiResponse<CalendarEventDto>> {
        val created = calendarService.createEvent(dto)
        return ResponseEntity.ok(ApiResponse.success(created))
    }

    @GetMapping("/{id}")
    fun getEvent(@PathVariable id: UUID): ResponseEntity<ApiResponse<CalendarEventDto>> {
        val event = calendarService.getEvent(id)
        return ResponseEntity.ok(ApiResponse.success(event))
    }

    @PutMapping("/{id}")
    fun updateEvent(
        @PathVariable id: UUID,
        @Valid @RequestBody dto: CalendarEventDto
    ): ResponseEntity<ApiResponse<CalendarEventDto>> {
        val updated = calendarService.updateEvent(id, dto)
        return ResponseEntity.ok(ApiResponse.success(updated))
    }

    @DeleteMapping("/{id}")
    fun deleteEvent(@PathVariable id: UUID): ResponseEntity<ApiResponse<Void>> {
        calendarService.deleteEvent(id)
        return ResponseEntity.ok(ApiResponse.success(null))
    }

    @GetMapping("/meal-plans")
    fun listMealPlanEvents(
        @RequestParam startDate: LocalDate,
        @RequestParam endDate: LocalDate
    ): ResponseEntity<ApiResponse<List<CalendarEventDto>>> {
        val events = calendarService.listMealPlanEvents(startDate, endDate)
        return ResponseEntity.ok(ApiResponse.success(events))
    }

    @PostMapping("/sync/google")
    fun syncGoogleCalendar(@Valid @RequestBody request: GoogleSyncRequest): ResponseEntity<ApiResponse<Void>> {
        calendarService.syncGoogleCalendar(request)
        return ResponseEntity.ok(ApiResponse.success(null))
    }
}

data class GoogleSyncRequest(
    val calendarId: String = "",
    val accessToken: String = ""
)
