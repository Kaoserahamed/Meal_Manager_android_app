package com.mealmanager.backend.service

import com.mealmanager.backend.model.dto.MealPlanDto
import com.mealmanager.backend.model.dto.MealAssignmentDto
import com.mealmanager.backend.model.dto.MealPlanTemplateDto
import com.mealmanager.backend.model.dto.GroceryListDto
import com.mealmanager.backend.model.dto.Page
import com.mealmanager.backend.repository.MealPlanRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional
class MealPlanService(
    private val mealPlanRepository: MealPlanRepository,
    private val groceryService: GroceryService
) {

    fun listMealPlans(page: Int, size: Int): Page<MealPlanDto> {
        val plans = mealPlanRepository.findAll(page, size)
        return Page(
            content = plans.map { it.toDto() },
            page = page,
            size = size,
            total = mealPlanRepository.count()
        )
    }

    fun getMealPlanById(id: UUID): MealPlanDto {
        return mealPlanRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Meal plan not found: $id") }
            .toDto()
    }

    fun createMealPlan(dto: MealPlanDto): MealPlanDto {
        val entity = dto.toEntity()
        val saved = mealPlanRepository.save(entity)
        return saved.toDto()
    }

    fun updateMealPlan(id: UUID, dto: MealPlanDto): MealPlanDto {
        val entity = mealPlanRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Meal plan not found: $id") }
        entity.updateFromDto(dto)
        val saved = mealPlanRepository.save(entity)
        return saved.toDto()
    }

    fun deleteMealPlan(id: UUID) {
        if (!mealPlanRepository.existsById(id)) {
            throw IllegalArgumentException("Meal plan not found: $id")
        }
        mealPlanRepository.deleteById(id)
    }

    fun updateMeals(id: UUID, meals: List<MealAssignmentDto>): MealPlanDto {
        val entity = mealPlanRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Meal plan not found: $id") }
        entity.meals = meals.map { it.toEntity() }
        val saved = mealPlanRepository.save(entity)
        return saved.toDto()
    }

    fun getGroceryList(planId: UUID): GroceryListDto {
        val plan = mealPlanRepository.findById(planId)
            .orElseThrow { IllegalArgumentException("Meal plan not found: $planId") }
        return groceryService.generateFromPlan(plan)
    }

    fun createTemplate(planId: UUID, template: MealPlanTemplateDto): MealPlanTemplateDto {
        val entity = template.toEntity(planId)
        val saved = mealPlanRepository.saveTemplate(entity)
        return saved.toDto()
    }

    fun listTemplates(): List<MealPlanTemplateDto> {
        return mealPlanRepository.findAllTemplates().map { it.toDto() }
    }
}
