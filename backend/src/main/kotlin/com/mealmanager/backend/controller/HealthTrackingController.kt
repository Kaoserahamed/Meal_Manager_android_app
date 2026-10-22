package com.mealmanager.backend.controller

import com.mealmanager.backend.model.dto.WeightLogDto
import com.mealmanager.backend.model.dto.WaterLogDto
import com.mealmanager.backend.model.dto.FastingWindowDto
import com.mealmanager.backend.model.dto.ApiResponse
import com.mealmanager.backend.service.WeightService
import com.mealmanager.backend.service.WaterService
import com.mealmanager.backend.service.FastingService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid
import java.util.UUID
import java.time.LocalDate

@RestController
@RequestMapping("/api/v1/weight")
class WeightController(
    private val weightService: WeightService
) {

    @GetMapping
    fun listWeightLogs(): ResponseEntity<ApiResponse<List<WeightLogDto>>> {
        val logs = weightService.listWeightLogs()
        return ResponseEntity.ok(ApiResponse.success(logs))
    }

    @PostMapping
    fun addWeightLog(@Valid @RequestBody log: WeightLogDto): ResponseEntity<ApiResponse<WeightLogDto>> {
        val created = weightService.addWeightLog(log)
        return ResponseEntity.ok(ApiResponse.success(created))
    }

    @GetMapping("/bmi")
    fun getBmi(): ResponseEntity<ApiResponse<BmiDto>> {
        val bmi = weightService.getBmi()
        return ResponseEntity.ok(ApiResponse.success(bmi))
    }

    @GetMapping("/progress-chart")
    fun getWeightProgressChart(): ResponseEntity<ApiResponse<WeightProgressChartDto>> {
        val chart = weightService.getWeightProgressChart()
        return ResponseEntity.ok(ApiResponse.success(chart))
    }
}

@RestController
@RequestMapping("/api/v1/water")
class WaterController(
    private val waterService: WaterService
) {

    @GetMapping
    fun listWaterLogs(): ResponseEntity<ApiResponse<List<WaterLogDto>>> {
        val logs = waterService.listWaterLogs()
        return ResponseEntity.ok(ApiResponse.success(logs))
    }

    @PostMapping
    fun addWaterLog(@Valid @RequestBody log: WaterLogDto): ResponseEntity<ApiResponse<WaterLogDto>> {
        val created = waterService.addWaterLog(log)
        return ResponseEntity.ok(ApiResponse.success(created))
    }

    @GetMapping("/daily-total/{date}")
    fun getDailyTotal(@PathVariable date: LocalDate): ResponseEntity<ApiResponse<Double>> {
        val total = waterService.getDailyTotal(date)
        return ResponseEntity.ok(ApiResponse.success(total))
    }

    @PostMapping("/reminders")
    fun setReminder(@Valid @RequestBody request: WaterReminderRequest): ResponseEntity<ApiResponse<Void>> {
        waterService.setReminder(request)
        return ResponseEntity.ok(ApiResponse.success(null))
    }
}

@RestController
@RequestMapping("/api/v1/fasting")
class FastingController(
    private val fastingService: FastingService
) {

    @GetMapping
    fun listFastingWindows(): ResponseEntity<ApiResponse<List<FastingWindowDto>>> {
        val windows = fastingService.listFastingWindows()
        return ResponseEntity.ok(ApiResponse.success(windows))
    }

    @PostMapping
    fun startFastingWindow(@Valid @RequestBody window: FastingWindowDto): ResponseEntity<ApiResponse<FastingWindowDto>> {
        val created = fastingService.startFastingWindow(window)
        return ResponseEntity.ok(ApiResponse.success(created))
    }

    @PutMapping("/{id}/end")
    fun endFastingWindow(@PathVariable id: UUID): ResponseEntity<ApiResponse<FastingWindowDto>> {
        val ended = fastingService.endFastingWindow(id)
        return ResponseEntity.ok(ApiResponse.success(ended))
    }

    @GetMapping("/history")
    fun getFastingHistory(): ResponseEntity<ApiResponse<List<FastingHistoryDto>>> {
        val history = fastingService.getFastingHistory()
        return ResponseEntity.ok(ApiResponse.success(history))
    }
}

data class BmiDto(
    val value: Double,
    val category: String
)

data class WeightProgressChartDto(
    val dates: List<String>,
    val weights: List<Double>
)

data class WaterReminderRequest(
    val intervalMinutes: Int,
    val enabled: Boolean
)

data class FastingHistoryDto(
    val date: String,
    val durationHours: Double,
    val completed: Boolean
)
