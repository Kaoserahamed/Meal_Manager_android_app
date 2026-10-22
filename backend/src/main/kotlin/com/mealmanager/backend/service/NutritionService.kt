package com.mealmanager.backend.service

import com.mealmanager.backend.model.dto.NutritionLogDto
import com.mealmanager.backend.model.dto.NutritionGoalDto
import com.mealmanager.backend.model.dto.DailyTotalsDto
import com.mealmanager.backend.model.dto.NutritionProgressDto
import com.mealmanager.backend.model.dto.NutritionTrendsDto
import com.mealmanager.backend.repository.NutritionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
@Transactional
class NutritionService(
    private val nutritionRepository: NutritionRepository
) {

    fun listNutritionLogs(startDate: LocalDate, endDate: LocalDate): List<NutritionLogDto> {
        return nutritionRepository.findByDateRange(startDate, endDate)
            .map { it.toDto() }
    }

    fun createNutritionLog(dto: NutritionLogDto): NutritionLogDto {
        val entity = dto.toEntity()
        entity.totals = calculateTotals(entity.meals)
        val saved = nutritionRepository.save(entity)
        return saved.toDto()
    }

    fun getNutritionGoals(): NutritionGoalDto {
        return nutritionRepository.findGoals()
            .map { it.toDto() }
            .orElseGet { NutritionGoalDto.default() }
    }

    fun updateNutritionGoals(dto: NutritionGoalDto): NutritionGoalDto {
        val entity = dto.toEntity()
        val saved = nutritionRepository.saveGoals(entity)
        return saved.toDto()
    }

    fun getDailyTotals(date: LocalDate): DailyTotalsDto {
        val logs = nutritionRepository.findByDate(date)
        val totals = logs.fold(DailyTotalsDto(0.0, 0.0, 0.0, 0.0)) { acc, log ->
            acc.copy(
                calories = acc.calories + log.totals.calories,
                protein = acc.protein + log.totals.protein,
                carbs = acc.carbs + log.totals.carbs,
                fat = acc.fat + log.totals.fat
            )
        }
        return totals
    }

    fun getProgress(): NutritionProgressDto {
        val goals = getNutritionGoals()
        val today = LocalDate.now()
        val totals = getDailyTotals(today)
        return NutritionProgressDto(
            caloriesPercent = if (goals.calories > 0) totals.calories / goals.calories * 100 else 0.0,
            proteinPercent = if (goals.protein > 0) totals.protein / goals.protein * 100 else 0.0,
            carbsPercent = if (goals.carbs > 0) totals.carbs / goals.carbs * 100 else 0.0,
            fatPercent = if (goals.fat > 0) totals.fat / goals.fat * 100 else 0.0
        )
    }

    fun getNutritionTrends(range: String): NutritionTrendsDto {
        val logs = nutritionRepository.findTrends(range)
        return NutritionTrendsDto(
            range = range,
            dailyAverages = logs.groupBy { it.date }
                .mapValues { (date, entries) ->
                    DailyAverageDto(
                        date = date,
                        calories = entries.averageOf { it.totals.calories },
                        protein = entries.averageOf { it.totals.protein },
                        carbs = entries.averageOf { it.totals.carbs },
                        fat = entries.averageOf { it.totals.fat }
                    )
                }
        )
    }

    private fun calculateTotals(meals: List<MealAssignmentDto>): DailyTotalsDto {
        return meals.fold(DailyTotalsDto(0.0, 0.0, 0.0, 0.0)) { acc, meal ->
            acc.copy(
                calories = acc.calories + meal.calories,
                protein = acc.protein + meal.protein,
                carbs = acc.carbs + meal.carbs,
                fat = acc.fat + meal.fat
            )
        }
    }
}

data class DailyAverageDto(
    val date: String,
    val calories: Double,
    val protein: Double,
    val carbs: Double,
    val fat: Double
)
