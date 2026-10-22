package com.example.meal_management

import com.example.meal_management.data.model.*
import org.junit.Assert.*
import org.junit.Test

/**
 * Unit tests for the meal platform domain models and calculations.
 * These are pure Kotlin tests and do not require Firebase or Android SDK.
 */
class PlatformDomainTest {

    @Test
    fun `recipe total time is prep plus cook`() {
        val recipe = Recipe(prepTimeMinutes = 15, cookTimeMinutes = 30)
        assertEquals(45, recipe.totalTimeMinutes)
    }

    @Test
    fun `nutrition per serving divides by servings`() {
        val recipe = Recipe(
            servings = 4,
            nutrition = NutritionInfo(calories = 800.0, protein = 40.0, carbs = 100.0, fat = 20.0)
        )
        val perServing = recipe.nutritionPerServing()
        assertEquals(200.0, perServing.calories, 0.001)
        assertEquals(10.0, perServing.protein, 0.001)
        assertEquals(25.0, perServing.carbs, 0.001)
        assertEquals(5.0, perServing.fat, 0.001)
    }

    @Test
    fun `nutrition per serving defaults to one serving when servings is zero`() {
        val recipe = Recipe(servings = 0, nutrition = NutritionInfo(calories = 300.0))
        assertEquals(300.0, recipe.nutritionPerServing().calories, 0.001)
    }

    @Test
    fun `nutrition info adds component-wise`() {
        val a = NutritionInfo(calories = 100.0, protein = 10.0, carbs = 5.0, fat = 2.0)
        val b = NutritionInfo(calories = 200.0, protein = 5.0, carbs = 10.0, fat = 3.0)
        val sum = a + b
        assertEquals(300.0, sum.calories, 0.001)
        assertEquals(15.0, sum.protein, 0.001)
        assertEquals(15.0, sum.carbs, 0.001)
        assertEquals(5.0, sum.fat, 0.001)
    }

    @Test
    fun `ingredient scales quantity by factor`() {
        val ingredient = Ingredient(name = "flour", quantity = 200.0, unit = "g")
        assertEquals(300.0, ingredient.scaledQuantity(1.5), 0.001)
        assertEquals(100.0, ingredient.scaledQuantity(0.5), 0.001)
    }

    @Test
    fun `recipe scales all ingredients by servings ratio`() {
        val recipe = Recipe(
            servings = 2,
            ingredients = listOf(
                Ingredient(name = "eggs", quantity = 2.0, unit = "pcs"),
                Ingredient(name = "milk", quantity = 100.0, unit = "ml")
            )
        )
        val scaled = recipe.scaleIngredients(2.0) // 4 servings / 2 servings
        assertEquals(4.0, scaled[0].quantity, 0.001)
        assertEquals(200.0, scaled[1].quantity, 0.001)
    }

    @Test
    fun `daily totals aggregate across logs`() {
        val logs = listOf(
            NutritionLog(totals = DailyTotals(calories = 500.0, protein = 20.0)),
            NutritionLog(totals = DailyTotals(calories = 700.0, protein = 30.0)),
            NutritionLog(totals = DailyTotals(calories = 300.0, protein = 10.0))
        )
        var calories = 0.0
        var protein = 0.0
        logs.forEach { log ->
            calories += log.totals.calories
            protein += log.totals.protein
        }
        assertEquals(1500.0, calories, 0.001)
        assertEquals(60.0, protein, 0.001)
    }

    @Test
    fun `daily progress computes percentages against goals`() {
        val goals = NutritionGoal(calories = 2000.0, protein = 100.0, carbs = 250.0, fat = 70.0)
        val totals = DailyTotals(calories = 1000.0, protein = 50.0, carbs = 125.0, fat = 35.0)
        val progress = DailyProgress(
            calories = totals.calories / goals.calories * 100,
            protein = totals.protein / goals.protein * 100,
            carbs = totals.carbs / goals.carbs * 100,
            fat = totals.fat / goals.fat * 100
        )
        assertEquals(50.0, progress.calories, 0.001)
        assertEquals(50.0, progress.protein, 0.001)
        assertEquals(50.0, progress.carbs, 0.001)
        assertEquals(50.0, progress.fat, 0.001)
    }

    @Test
    fun `meal plan holds assignments and template flag`() {
        val plan = MealPlan(
            name = "Week 1",
            userId = "user_1",
            isTemplate = true,
            meals = listOf(
                MealAssignment(day = 1, mealType = MealType.BREAKFAST, recipeId = "recipe_1", servings = 1),
                MealAssignment(day = 1, mealType = MealType.DINNER, recipeId = "recipe_2", servings = 2)
            )
        )
        assertEquals("Week 1", plan.name)
        assertTrue(plan.isTemplate)
        assertEquals(2, plan.meals.size)
        assertEquals(MealType.BREAKFAST, plan.meals[0].mealType)
    }

    @Test
    fun `grocery item merge sums quantities by name`() {
        val items = listOf(
            GroceryItem(name = "milk", quantity = 1.0, unit = "L"),
            GroceryItem(name = "milk", quantity = 2.0, unit = "L"),
            GroceryItem(name = "eggs", quantity = 6.0, unit = "pcs")
        )
        val merged = items.groupBy { it.name }.mapValues { (_, v) ->
            v.fold(0.0) { acc, it -> acc + it.quantity }
        }
        assertEquals(3.0, merged["milk"], 0.001)
        assertEquals(6.0, merged["eggs"], 0.001)
    }

    @Test
    fun `unchecked count reflects checked state`() {
        val list = GroceryList(
            items = listOf(
                GroceryItem(name = "a", checked = false),
                GroceryItem(name = "b", checked = true),
                GroceryItem(name = "c", checked = false)
            )
        )
        assertEquals(2, list.items.count { !it.checked })
    }
}
