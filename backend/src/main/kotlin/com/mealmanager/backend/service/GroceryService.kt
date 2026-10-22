package com.mealmanager.backend.service

import com.mealmanager.backend.model.dto.GroceryListDto
import com.mealmanager.backend.model.dto.GroceryItemDto
import com.mealmanager.backend.model.dto.Page
import com.mealmanager.backend.repository.GroceryRepository
import com.mealmanager.backend.repository.MealPlanRepository
import com.mealmanager.backend.repository.RecipeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional
class GroceryService(
    private val groceryRepository: GroceryRepository,
    private val mealPlanRepository: MealPlanRepository,
    private val recipeRepository: RecipeRepository
) {

    fun listGroceryLists(page: Int, size: Int): Page<GroceryListDto> {
        val lists = groceryRepository.findAll(page, size)
        return Page(
            content = lists.map { it.toDto() },
            page = page,
            size = size,
            total = groceryRepository.count()
        )
    }

    fun getGroceryListById(id: UUID): GroceryListDto {
        return groceryRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Grocery list not found: $id") }
            .toDto()
    }

    fun generateFromPlan(plan: com.mealmanager.backend.model.entity.MealPlanEntity): GroceryListDto {
        val aggregated = mutableMapOf<String, MutableList<GroceryItemDto>>()
        plan.meals.forEach { assignment ->
            val recipe = recipeRepository.findById(assignment.recipeId)
                .orElseThrow { IllegalArgumentException("Recipe not found: ${assignment.recipeId}") }
            val scale = assignment.servings.toDouble() / if (recipe.servings > 0) recipe.servings else 1.0
            recipe.scaledIngredients(scale).forEach { ingredient ->
                aggregated.getOrPut(ingredient.name) { mutableListOf() }.add(ingredient)
            }
        }
        val items = aggregated.entries.map { (name, ingredients) ->
            val totalQty = ingredients.sumOf { it.quantity }
            val unit = ingredients.firstOrNull { it.unit.isNotBlank() }?.unit ?: ""
            GroceryItemDto(
                id = UUID.randomUUID(),
                name = name,
                quantity = totalQty,
                unit = unit,
                category = categorize(name),
                aisle = aisleFor(name),
                checked = false,
                notes = ""
            )
        }.sortedBy { it.category }
        val listDto = GroceryListDto(
            id = UUID.randomUUID(),
            mealPlanId = plan.id,
            userId = plan.userId,
            items = items,
            generatedAt = java.time.Instant.now().toString()
        )
        val saved = groceryRepository.save(listDto.toEntity())
        return saved.toDto()
    }

    fun generateGroceryList(mealPlanId: UUID): GroceryListDto {
        val plan = mealPlanRepository.findById(mealPlanId)
            .orElseThrow { IllegalArgumentException("Meal plan not found: $mealPlanId") }
        return generateFromPlan(plan)
    }

    fun toggleItemChecked(listId: UUID, itemId: UUID, checked: Boolean) {
        val list = groceryRepository.findById(listId)
            .orElseThrow { IllegalArgumentException("Grocery list not found: $listId") }
        val item = list.items.find { it.id == itemId }
            .orElseThrow { IllegalArgumentException("Item not found: $itemId") }
        item.checked = checked
        groceryRepository.save(list.toEntity())
    }

    fun deleteGroceryList(id: UUID) {
        if (!groceryRepository.existsById(id)) {
            throw IllegalArgumentException("Grocery list not found: $id")
        }
        groceryRepository.deleteById(id)
    }

    fun getCategories(): List<String> {
        return listOf("Dairy", "Produce", "Meat", "Pantry", "Other")
    }

    fun getAisles(): List<String> {
        return listOf("Aisle 1", "Aisle 2", "Aisle 3", "Aisle 4", "Aisle 5", "Aisle 6")
    }

    private fun categorize(name: String): String {
        val lower = name.lowercase()
        return when {
            lower.contains("milk") || lower.contains("cheese") || lower.contains("yogurt") || lower.contains("butter") || lower.contains("egg") -> "Dairy"
            lower.contains("apple") || lower.contains("banana") || lower.contains("orange") || lower.contains("berry") || lower.contains("fruit") || lower.contains("potato") || lower.contains("tomato") || lower.contains("onion") || lower.contains("carrot") || lower.contains("lettuce") || lower.contains("vegetable") -> "Produce"
            lower.contains("chicken") || lower.contains("beef") || lower.contains("pork") || lower.contains("fish") || lower.contains("meat") || lower.contains("turkey") -> "Meat"
            lower.contains("bread") || lower.contains("rice") || lower.contains("pasta") || lower.contains("flour") || lower.contains("cereal") || lower.contains("grain") -> "Pantry"
            else -> "Other"
        }
    }

    private fun aisleFor(name: String): String {
        return when (categorize(name)) {
            "Dairy" -> "Aisle 4"
            "Produce" -> "Aisle 1"
            "Meat" -> "Aisle 5"
            "Pantry" -> "Aisle 3"
            else -> "Aisle 6"
        }
    }
}
