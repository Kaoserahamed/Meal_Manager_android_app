package com.example.meal_management

import com.example.meal_management.data.model.*
import org.junit.Assert.*
import org.junit.Test

class PhaseDomainTest {

    @Test
    fun `barcode scan result tracks found state`() {
        val result = BarcodeScanResult(barcode = "123456", found = true, foodName = "Milk", nutrition = NutritionInfo(calories = 150.0))
        assertTrue(result.found)
        assertEquals("Milk", result.foodName)
        assertEquals(150.0, result.nutrition!!.calories, 0.001)
    }

    @Test
    fun `packaged food has nutrition`() {
        val food = PackagedFood(barcode = "0001", name = "Almond Milk", brand = "BrandX", nutrition = NutritionInfo(calories = 60.0, protein = 1.0, carbs = 8.0, fat = 3.0))
        assertEquals("Almond Milk", food.name)
        assertEquals(60.0, food.nutrition.calories, 0.001)
    }

    @Test
    fun `fridge item tracks expiration`() {
        val item = FridgeItem(name = "Yogurt", quantity = 2.0, unit = "cups", category = "Dairy", expirationDate = System.currentTimeMillis() + 86400000, lowStockThreshold = 1)
        assertEquals("Dairy", item.category)
        assertTrue(item.expirationDate > System.currentTimeMillis())
    }

    @Test
    fun `pantry item below threshold detection`() {
        val item = PantryItem(name = "Rice", quantity = 0.5, unit = "kg", category = "Pantry", reorderThreshold = 2, estimatedReorderCost = 3.50)
        assertTrue(item.quantity < item.reorderThreshold)
    }

    @Test
    fun `weight log bmi inputs`() {
        val log = WeightLog(userId = "user_1", date = System.currentTimeMillis(), weightKg = 70.0, bodyFatPercent = 18.0)
        assertEquals(70.0, log.weightKg, 0.001)
    }

    @Test
    fun `water log accumulates`() {
        val logs = listOf(WaterLog(date = 1L, amountMl = 250.0), WaterLog(date = 1L, amountMl = 500.0))
        assertEquals(750.0, logs.sumOf { it.amountMl }, 0.001)
    }

    @Test
    fun `fasting window duration`() {
        val start = System.currentTimeMillis()
        val end = start + 16 * 3600000L
        val window = FastingWindow(userId = "user_1", startTime = start, endTime = end, durationHours = 16.0, completed = true)
        assertEquals(16.0, window.durationHours, 0.001)
        assertTrue(window.completed)
    }

    @Test
    fun `meal photo recognized food`() {
        val photo = MealPhoto(userId = "user_1", photoUrl = "https://example.com/photo.jpg", recognizedFood = "Salad", confidence = 0.95)
        assertEquals("Salad", photo.recognizedFood)
        assertTrue(photo.confidence >= 0.9)
    }

    @Test
    fun `ai recognition result carries nutrition`() {
        val result = AIFoodRecognitionResult(foodName = "Apple", confidence = 0.9, estimatedPortionGrams = 150.0, nutrition = NutritionInfo(calories = 95.0))
        assertEquals("Apple", result.foodName)
        assertEquals(95.0, result.nutrition.calories, 0.001)
    }

    @Test
    fun `smart suggestion references recipes`() {
        val suggestion = SmartSuggestion(itemName = "Eggs", suggestedRecipeIds = listOf("recipe_1", "recipe_2"), reason = "Based on fridge ingredient")
        assertEquals(2, suggestion.suggestedRecipeIds.size)
    }

    @Test
    fun `prep step has duration`() {
        val step = PrepStep(stepNumber = 1, instruction = "Chop onions", durationMinutes = 5)
        assertEquals(5, step.durationMinutes)
    }

    @Test
    fun `make ahead meal has expiration`() {
        val meal = MakeAheadMeal(id = "meal_1", name = "Soup", storageInstructions = "Refrigerate", expirationDate = "2025-01-15")
        assertEquals("Soup", meal.name)
    }

    @Test
    fun `cooking timer tracks remaining`() {
        val timer = CookingTimer(id = "timer_1", recipeId = "recipe_1", stepIndex = 0, durationSeconds = 300, remainingSeconds = 300, running = true)
        assertTrue(timer.running)
        assertEquals(300, timer.remainingSeconds)
    }

    @Test
    fun `cooking mode hands free`() {
        val mode = CookingMode(recipeId = "recipe_1", currentStep = 0, handsFree = true)
        assertTrue(mode.handsFree)
    }

    @Test
    fun `voice shortcut maps phrase to action`() {
        val shortcut = VoiceShortcut(phrase = "Log breakfast", action = "log_meal")
        assertEquals("log_meal", shortcut.action)
    }

    @Test
    fun `wearable sync records count`() {
        val sync = WearableSync(syncedAt = System.currentTimeMillis(), recordsSynced = 100)
        assertEquals(100, sync.recordsSynced)
    }

    @Test
    fun `google fit data has steps`() {
        val fit = GoogleFitData(steps = 10000, calories = 400.0, activeMinutes = 60)
        assertEquals(10000, fit.steps)
    }

    @Test
    fun `health kit data has workouts`() {
        val hk = HealthKitData(steps = 9000, calories = 380.0, workouts = 4)
        assertEquals(4, hk.workouts)
    }

    @Test
    fun `audit log records action`() {
        val log = AuditLog(userId = "user_1", action = "CREATE_RECIPE", details = "Created pasta recipe")
        assertEquals("CREATE_RECIPE", log.action)
    }

    @Test
    fun `feature flag enabled state`() {
        val flag = FeatureFlag(key = "dark_mode", enabled = true, description = "Dark mode toggle")
        assertTrue(flag.enabled)
    }

    @Test
    fun `ab test experiment status`() {
        val experiment = AbTestExperiment(name = "Onboarding Flow", status = "active", variants = listOf("control", "treatment"))
        assertEquals("active", experiment.status)
        assertEquals(2, experiment.variants.size)
    }

    @Test
    fun `ab test assignment variant`() {
        val assignment = AbTestAssignment(experimentId = "exp_1", userId = "user_1", variant = "treatment")
        assertEquals("treatment", assignment.variant)
    }

    @Test
    fun `ab test result conversion`() {
        val result = AbTestResult(experimentId = "exp_1", userId = "user_1", variant = "control", converted = false)
        assertFalse(result.converted)
    }
}
