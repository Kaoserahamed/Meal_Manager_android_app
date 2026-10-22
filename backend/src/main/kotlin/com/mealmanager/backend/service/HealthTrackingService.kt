package com.mealmanager.backend.service

import com.mealmanager.backend.model.dto.WeightLogDto
import com.mealmanager.backend.model.dto.WaterLogDto
import com.mealmanager.backend.model.dto.BmiDto
import com.mealmanager.backend.model.dto.WeightProgressChartDto
import com.mealmanager.backend.model.dto.FastingWindowDto
import com.mealmanager.backend.model.dto.FastingHistoryDto
import com.mealmanager.backend.repository.WeightRepository
import com.mealmanager.backend.repository.WaterRepository
import com.mealmanager.backend.repository.FastingRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.UUID

@Service
@Transactional
class WeightService(
    private val weightRepository: WeightRepository
) {

    fun listWeightLogs(): List<WeightLogDto> {
        return weightRepository.findAll().map { it.toDto() }
    }

    fun addWeightLog(dto: WeightLogDto): WeightLogDto {
        val entity = dto.toEntity()
        val saved = weightRepository.save(entity)
        return saved.toDto()
    }

    fun getBmi(): BmiDto {
        val latest = weightRepository.findLatest()
        if (latest == null) {
            return BmiDto(value = 0.0, category = "Unknown")
        }
        val heightM = 1.75 // placeholder; would come from profile
        val bmi = latest.weightKg / (heightM * heightM)
        val category = when {
            bmi < 18.5 -> "Underweight"
            bmi < 25 -> "Normal"
            bmi < 30 -> "Overweight"
            else -> "Obese"
        }
        return BmiDto(value = bmi, category = category)
    }

    fun getWeightProgressChart(): WeightProgressChartDto {
        val logs = weightRepository.findAll()
        return WeightProgressChartDto(
            dates = logs.map { it.date.toString() },
            weights = logs.map { it.weightKg }
        )
    }
}

@Service
@Transactional
class WaterService(
    private val waterRepository: WaterRepository
) {

    fun listWaterLogs(): List<WaterLogDto> {
        return waterRepository.findAll().map { it.toDto() }
    }

    fun addWaterLog(dto: WaterLogDto): WaterLogDto {
        val entity = dto.toEntity()
        val saved = waterRepository.save(entity)
        return saved.toDto()
    }

    fun getDailyTotal(date: LocalDate): Double {
        return waterRepository.findByDate(date).sumOf { it.amountMl }
    }

    fun setReminder(request: com.mealmanager.backend.controller.WaterReminderRequest) {
        waterRepository.setReminder(request.userId, request.intervalMinutes, request.enabled)
    }
}

@Service
@Transactional
class FastingService(
    private val fastingRepository: FastingRepository
) {

    fun listFastingWindows(): List<FastingWindowDto> {
        return fastingRepository.findAll().map { it.toDto() }
    }

    fun startFastingWindow(dto: FastingWindowDto): FastingWindowDto {
        val entity = dto.toEntity()
        val saved = fastingRepository.save(entity)
        return saved.toDto()
    }

    fun endFastingWindow(id: UUID): FastingWindowDto {
        val entity = fastingRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Fasting window not found: $id") }
        entity.endTime = java.time.Instant.now()
        val saved = fastingRepository.save(entity)
        return saved.toDto()
    }

    fun getFastingHistory(): List<FastingHistoryDto> {
        return fastingRepository.findAll().map { it.toHistoryDto() }
    }
}
