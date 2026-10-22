package com.mealmanager.backend.service

import com.mealmanager.backend.model.dto.RecipeDto
import com.mealmanager.backend.repository.RecipeRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.util.UUID

@ExtendWith(MockitoExtension::class)
class RecipeServiceTest {

    @Mock
    private lateinit var recipeRepository: RecipeRepository

    @InjectMocks
    private lateinit var recipeService: RecipeService

    @BeforeEach
    fun setUp() {
        recipeService = RecipeService(recipeRepository)
    }

    @Test
    fun `createRecipe saves and returns dto`() {
        val dto = RecipeDto(title = "Pasta", servings = 2)
        val entity = com.mealmanager.backend.model.entity.RecipeEntity(title = "Pasta", servings = 2)
        `when`(recipeRepository.save(any())).thenAnswer { it.getArgument<RecipeEntity>(0) }
        val result = recipeService.createRecipe(dto)
        assertEquals("Pasta", result.title)
        verify(recipeRepository, times(1)).save(any())
    }

    @Test
    fun `getRecipeById throws when missing`() {
        val id = UUID.randomUUID()
        `when`(recipeRepository.findById(id)).thenReturn(java.util.Optional.empty())
        assertThrows(IllegalArgumentException::class) { recipeService.getRecipeById(id) }
    }

    @Test
    fun `deleteRecipe removes existing recipe`() {
        val id = UUID.randomUUID()
        `when`(recipeRepository.existsById(id)).thenReturn(true)
        recipeService.deleteRecipe(id)
        verify(recipeRepository, times(1)).deleteById(id)
    }

    @Test
    fun `scaleRecipe adjusts servings`() {
        val id = UUID.randomUUID()
        val entity = com.mealmanager.backend.model.entity.RecipeEntity(servings = 2)
        `when`(recipeRepository.findById(id)).thenReturn(java.util.Optional.of(entity))
        `when`(recipeRepository.save(any())).thenAnswer { it.getArgument<RecipeEntity>(0) }
        val result = recipeService.scaleRecipe(id, 4)
        assertEquals(4, result.servings)
    }
}
