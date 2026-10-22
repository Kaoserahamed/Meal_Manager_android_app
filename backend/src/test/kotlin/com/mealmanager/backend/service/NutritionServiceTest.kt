package com.mealmanager.backend.service

import com.mealmanager.backend.model.dto.NutritionGoalDto
import com.mealmanager.backend.repository.NutritionRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class NutritionServiceTest {

    @Mock
    private lateinit var nutritionRepository: NutritionRepository

    @InjectMocks
    private lateinit var nutritionService: NutritionService

    @BeforeEach
    fun setUp() {
        nutritionService = NutritionService(nutritionRepository)
    }

    @Test
    fun `getProgress computes percentages`() {
        `when`(nutritionRepository.findGoals()).thenReturn(java.util.Optional.of(
            com.mealmanager.backend.model.entity.NutritionGoalEntity(calories = 2000.0, protein = 100.0)
        ))
        val progress = nutritionService.getProgress()
        assertNotNull(progress)
        assertTrue(progress.caloriesPercent >= 0.0)
    }

    @Test
    fun `updateNutritionGoals saves goals`() {
        val dto = NutritionGoalDto(calories = 2500.0, protein = 120.0)
        val entity = dto.toEntity()
        `when`(nutritionRepository.saveGoals(any())).thenAnswer { it.getArgument<com.mealmanager.backend.model.entity.NutritionGoalEntity>(0) }
        val result = nutritionService.updateNutritionGoals(dto)
        assertEquals(2500.0, result.calories)
    }
}
