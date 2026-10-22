package com.mealmanager.backend.service

import com.mealmanager.backend.model.dto.PersonalizationDto
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MLRecommendationServiceTest {

    private lateinit var service: MLRecommendationService

    @BeforeEach
    fun setUp() { service = MLRecommendationService() }

    @Test
    fun `getRecipeRecommendations returns recommendations`() {
        val recs = service.getRecipeRecommendations()
        assertTrue(recs.isNotEmpty())
        assertTrue(recs.all { it.score > 0 })
    }

    @Test
    fun `getPersonalization returns personalization`() {
        val p = service.getPersonalization()
        assertTrue(p.preferredCuisine.isNotEmpty())
    }

    @Test
    fun `updatePersonalization returns dto`() {
        val dto = PersonalizationDto(preferredCuisine = listOf("Japanese"), dietaryTags = listOf("vegan"), spiceLevel = "hot")
        val result = service.updatePersonalization(dto)
        assertEquals("Japanese", result.preferredCuisine.first())
    }

    @Test
    fun `trainModel returns completed`() {
        val result = service.trainModel()
        assertEquals("completed", result.status)
    }
}
