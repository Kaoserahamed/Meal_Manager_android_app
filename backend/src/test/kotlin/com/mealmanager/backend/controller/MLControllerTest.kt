package com.mealmanager.backend.controller

import com.mealmanager.backend.service.MLRecommendationService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MLControllerTest {

    private lateinit var controller: MLController

    @BeforeEach
    fun setUp() { controller = MLController(MLRecommendationService()) }

    @Test
    fun `getRecipeRecommendations returns 200`() {
        val response = controller.getRecipeRecommendations()
        assertEquals(200, response.statusCode.value())
    }

    @Test
    fun `getPersonalization returns 200`() {
        val response = controller.getPersonalization()
        assertEquals(200, response.statusCode.value())
    }

    @Test
    fun `trainModel returns 200`() {
        val response = controller.trainModel()
        assertEquals(200, response.statusCode.value())
    }
}
