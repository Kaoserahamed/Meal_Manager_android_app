package com.example.meal_management

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class PhaseInstrumentedTest {

    @Test
    fun useAppContext() {
        assertEquals("com.example.meal_management", InstrumentationRegistry.getInstrumentation().targetContext.packageName)
    }

    @Test
    fun barcodeModelParcelableFields() {
        val result = com.example.meal_management.data.model.BarcodeScanResult(
            barcode = "890123",
            found = true,
            foodName = "Cereal",
            nutrition = com.example.meal_management.data.model.NutritionInfo(calories = 200.0)
        )
        assertTrue(result.found)
        assertEquals("Cereal", result.foodName)
    }

    @Test
    fun phaseModelsAreNotNull() {
        assertNotNull(com.example.meal_management.data.model.FridgeItem(name = "Milk"))
        assertNotNull(com.example.meal_management.data.model.PantryItem(name = "Rice"))
        assertNotNull(com.example.meal_management.data.model.WeightLog(userId = "u", weightKg = 70.0))
        assertNotNull(com.example.meal_management.data.model.WaterLog(userId = "u", amountMl = 300.0))
        assertNotNull(com.example.meal_management.data.model.FastingWindow(userId = "u"))
    }
}
