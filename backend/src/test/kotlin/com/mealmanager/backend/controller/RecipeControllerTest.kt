package com.mealmanager.backend.controller

import com.mealmanager.backend.service.RecipeService
import com.mealmanager.backend.model.dto.RecipeDto
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.ResponseEntity
import java.util.UUID

@ExtendWith(MockitoExtension::class)
class RecipeControllerTest {

    @Mock
    private lateinit var recipeService: RecipeService

    @InjectMocks
    private lateinit var controller: RecipeController

    @BeforeEach
    fun setUp() {
        controller = RecipeController(recipeService)
    }

    @Test
    fun `createRecipe returns success response`() {
        val dto = RecipeDto(title = "Soup")
        val response = controller.createRecipe(dto)
        assertEquals(200, response.statusCode.value())
    }

    @Test
    fun `deleteRecipe returns success`() {
        val id = UUID.randomUUID()
        val response = controller.deleteRecipe(id)
        assertEquals(200, response.statusCode.value())
    }
}
