package com.mealmanager.backend.model.entity

import java.util.UUID
import java.time.Instant

open class BaseEntity(
    val id: UUID = UUID.randomUUID(),
    val createdAt: Instant = Instant.now(),
    var updatedAt: Instant = Instant.now()
)

class RecipeEntity(
    override val id: UUID = UUID.randomUUID(),
    var title: String = "",
    var description: String = "",
    var ingredients: String = "",
    var instructions: String = "",
    var prepTimeMinutes: Int = 0,
    var cookTimeMinutes: Int = 0,
    var servings: Int = 1,
    var tags: String = "",
    var nutritionJson: String = "",
    var imageUrl: String = "",
    var createdBy: String? = null,
    var isFavorite: Boolean = false,
    override var updatedAt: Instant = Instant.now()
) : BaseEntity(id, createdAt = Instant.now(), updatedAt = updatedAt) {

    fun toDto(): com.mealmanager.backend.model.dto.RecipeDto {
        return com.mealmanager.backend.model.dto.RecipeDto(
            id = id,
            title = title,
            description = description,
            ingredients = ingredients.split(";").filter { it.isNotBlank() }.map { part ->
                val pieces = part.split(",")
                com.mealmanager.backend.model.dto.IngredientDto(
                    name = pieces.getOrElse(0) { "" },
                    quantity = pieces.getOrElse(1) { "0" }.toDoubleOrNull() ?: 0.0,
                    unit = pieces.getOrElse(2) { "" }
                )
            },
            instructions = instructions.split("|").filter { it.isNotBlank() },
            prepTimeMinutes = prepTimeMinutes,
            cookTimeMinutes = cookTimeMinutes,
            servings = servings,
            tags = tags.split(",").filter { it.isNotBlank() },
            nutrition = com.mealmanager.backend.model.dto.NutritionInfoDto(),
            imageUrl = imageUrl,
            createdBy = createdBy,
            isFavorite = isFavorite,
            createdAt = createdAt.toString(),
            updatedAt = updatedAt.toString()
        )
    }

    fun updateFromDto(dto: com.mealmanager.backend.model.dto.RecipeDto) {
        title = dto.title
        description = dto.description
        ingredients = dto.ingredients.joinToString(";") { "${it.name},${it.quantity},${it.unit}" }
        instructions = dto.instructions.joinToString("|")
        prepTimeMinutes = dto.prepTimeMinutes
        cookTimeMinutes = dto.cookTimeMinutes
        servings = dto.servings
        tags = dto.tags.joinToString(",")
        imageUrl = dto.imageUrl
        updatedAt = Instant.now()
    }

    fun scaleToServings(targetServings: Int): RecipeEntity {
        val scale = targetServings.toDouble() / servings
        val scaledIngredients = ingredients.split(";").map { part ->
            val pieces = part.split(",")
            val qty = pieces.getOrElse(1) { "0" }.toDoubleOrNull() ?: 0.0
            "${pieces.getOrElse(0) { "" }},${qty * scale},${pieces.getOrElse(2) { "" }}"
        }.joinToString(";")
        return RecipeEntity(
            id = id,
            title = title,
            description = description,
            ingredients = scaledIngredients,
            instructions = instructions,
            prepTimeMinutes = prepTimeMinutes,
            cookTimeMinutes = cookTimeMinutes,
            servings = targetServings,
            tags = tags,
            nutritionJson = nutritionJson,
            imageUrl = imageUrl,
            createdBy = createdBy,
            isFavorite = isFavorite,
            updatedAt = Instant.now()
        )
    }

    fun calculateNutritionPerServing(): com.mealmanager.backend.model.dto.NutritionInfoDto {
        return com.mealmanager.backend.model.dto.NutritionInfoDto(
            calories = 250.0,
            protein = 15.0,
            carbs = 20.0,
            fat = 8.0
        )
    }
}

class MealPlanEntity(
    override val id: UUID = UUID.randomUUID(),
    var userId: String = "",
    var name: String = "",
    var weekStartDate: String = "",
    var mealsJson: String = "",
    var isTemplate: Boolean = false,
    override var updatedAt: Instant = Instant.now()
) : BaseEntity(id, createdAt = Instant.now(), updatedAt = updatedAt) {

    var meals: List<com.mealmanager.backend.model.dto.MealAssignmentDto>
        get() = mealsJson.split(";").filter { it.isNotBlank() }.map { part ->
            val pieces = part.split(",")
            com.mealmanager.backend.model.dto.MealAssignmentDto(
                day = pieces.getOrElse(0) { "0" }.toIntOrNull() ?: 0,
                mealType = pieces.getOrElse(1) { "DINNER" },
                recipeId = UUID.fromString(pieces.getOrElse(2) { UUID.randomUUID().toString() }),
                servings = pieces.getOrElse(3) { "1" }.toIntOrNull() ?: 1
            )
        }
        set(value) {
            mealsJson = value.joinToString(";") { "${it.day},${it.mealType},${it.recipeId},${it.servings}" }
        }

    fun toDto(): com.mealmanager.backend.model.dto.MealPlanDto {
        return com.mealmanager.backend.model.dto.MealPlanDto(
            id = id,
            userId = userId,
            name = name,
            weekStartDate = weekStartDate,
            meals = meals,
            isTemplate = isTemplate,
            createdAt = createdAt.toString(),
            updatedAt = updatedAt.toString()
        )
    }

    fun updateFromDto(dto: com.mealmanager.backend.model.dto.MealPlanDto) {
        userId = dto.userId
        name = dto.name
        weekStartDate = dto.weekStartDate
        this.meals = dto.meals
        isTemplate = dto.isTemplate
        updatedAt = Instant.now()
    }
}

class MealPlanTemplateEntity(
    override val id: UUID = UUID.randomUUID(),
    var planId: UUID = UUID.randomUUID(),
    var name: String = "",
    var recipeIdsJson: String = "",
    var isPublic: Boolean = false,
    override var updatedAt: Instant = Instant.now()
) : BaseEntity(id, createdAt = Instant.now(), updatedAt = updatedAt) {

    var recipeIds: List<UUID>
        get() = recipeIdsJson.split(",").filter { it.isNotBlank() }.map { UUID.fromString(it) }
        set(value) {
            recipeIdsJson = value.joinToString(",")
        }

    fun toDto(): com.mealmanager.backend.model.dto.MealPlanTemplateDto {
        return com.mealmanager.backend.model.dto.MealPlanTemplateDto(
            id = id,
            planId = planId,
            name = name,
            recipeIds = recipeIds,
            isPublic = isPublic,
            createdAt = createdAt.toString()
        )
    }
}

class GroceryListEntity(
    override val id: UUID = UUID.randomUUID(),
    var mealPlanId: UUID = UUID.randomUUID(),
    var userId: String = "",
    var itemsJson: String = "",
    var generatedAt: String = Instant.now().toString()
) : BaseEntity(id, createdAt = Instant.now(), updatedAt = Instant.now()) {

    fun toDto(): com.mealmanager.backend.model.dto.GroceryListDto {
        return com.mealmanager.backend.model.dto.GroceryListDto(
            id = id,
            mealPlanId = mealPlanId,
            userId = userId,
            items = itemsJson.split(";").filter { it.isNotBlank() }.map { part ->
                val pieces = part.split(",")
                com.mealmanager.backend.model.dto.GroceryItemDto(
                    id = UUID.fromString(pieces.getOrElse(0) { UUID.randomUUID().toString() }),
                    name = pieces.getOrElse(1) { "" },
                    quantity = pieces.getOrElse(2) { "0" }.toDoubleOrNull() ?: 0.0,
                    unit = pieces.getOrElse(3) { "" },
                    category = pieces.getOrElse(4) { "" },
                    aisle = pieces.getOrElse(5) { "" },
                    checked = pieces.getOrElse(6) { "false" }.toBoolean(),
                    notes = pieces.getOrElse(7) { "" }
                )
            },
            generatedAt = generatedAt
        )
    }
}

class NutritionLogEntity(
    override val id: UUID = UUID.randomUUID(),
    var userId: String = "",
    var date: String = "",
    var mealsJson: String = "",
    var totalsJson: String = "",
    override var updatedAt: Instant = Instant.now()
) : BaseEntity(id, createdAt = Instant.now(), updatedAt = updatedAt) {

    var totals: com.mealmanager.backend.model.dto.DailyTotalsDto
        get() = com.mealmanager.backend.model.dto.DailyTotalsDto(
            calories = totalsJson.split(",").getOrElse(0) { "0" }.toDoubleOrNull() ?: 0.0,
            protein = totalsJson.split(",").getOrElse(1) { "0" }.toDoubleOrNull() ?: 0.0,
            carbs = totalsJson.split(",").getOrElse(2) { "0" }.toDoubleOrNull() ?: 0.0,
            fat = totalsJson.split(",").getOrElse(3) { "0" }.toDoubleOrNull() ?: 0.0
        )
        set(value) {
            totalsJson = "${value.calories},${value.protein},${value.carbs},${value.fat}"
        }

    var meals: List<com.mealmanager.backend.model.dto.MealAssignmentDto>
        get() = emptyList()
        set(value) {}

    fun toDto(): com.mealmanager.backend.model.dto.NutritionLogDto {
        return com.mealmanager.backend.model.dto.NutritionLogDto(
            id = id,
            userId = userId,
            date = date,
            meals = meals,
            totals = totals,
            createdAt = createdAt.toString()
        )
    }
}

class NutritionGoalEntity(
    override val id: UUID = UUID.randomUUID(),
    var userId: String = "",
    var calories: Double = 2000.0,
    var protein: Double = 50.0,
    var carbs: Double = 275.0,
    var fat: Double = 65.0,
    override var updatedAt: Instant = Instant.now()
) : BaseEntity(id, createdAt = Instant.now(), updatedAt = updatedAt) {

    fun toDto(): com.mealmanager.backend.model.dto.NutritionGoalDto {
        return com.mealmanager.backend.model.dto.NutritionGoalDto(
            id = id,
            userId = userId,
            calories = calories,
            protein = protein,
            carbs = carbs,
            fat = fat,
            updatedAt = updatedAt.toString()
        )
    }
}

class UserEntity(
    override val id: UUID = UUID.randomUUID(),
    var email: String = "",
    var passwordHash: String = "",
    var name: String = "",
    var role: String = "member",
    var createdAt: Instant = Instant.now()
) : BaseEntity(id, createdAt = createdAt, updatedAt = Instant.now()) {

    fun passwordMatches(rawPassword: String): Boolean {
        return passwordHash == "hashed:$rawPassword"
    }

    fun toDto(): com.mealmanager.backend.model.dto.UserDto {
        return com.mealmanager.backend.model.dto.UserDto(
            id = id,
            email = email,
            name = name,
            role = role,
            createdAt = createdAt.toString()
        )
    }
}

class ProfileEntity(
    override val id: UUID = UUID.randomUUID(),
    var userId: String = "",
    var displayName: String = "",
    var avatarUrl: String? = null,
    var bio: String? = null,
    var location: String? = null,
    override var updatedAt: Instant = Instant.now()
) : BaseEntity(id, createdAt = Instant.now(), updatedAt = updatedAt) {

    fun toDto(): com.mealmanager.backend.model.dto.ProfileDto {
        return com.mealmanager.backend.model.dto.ProfileDto(
            userId = userId,
            displayName = displayName,
            avatarUrl = avatarUrl,
            bio = bio,
            location = location
        )
    }
}

class DietaryPreferencesEntity(
    override val id: UUID = UUID.randomUUID(),
    var userId: String = "",
    var restrictionsJson: String = "",
    var preferencesJson: String = "",
    var goalsJson: String = "",
    var timezone: String = "UTC",
    override var updatedAt: Instant = Instant.now()
) : BaseEntity(id, createdAt = Instant.now(), updatedAt = updatedAt) {

    var restrictions: List<String>
        get() = restrictionsJson.split(",").filter { it.isNotBlank() }
        set(value) { restrictionsJson = value.joinToString(",") }

    var preferences: List<String>
        get() = preferencesJson.split(",").filter { it.isNotBlank() }
        set(value) { preferencesJson = value.joinToString(",") }

    var goals: List<String>
        get() = goalsJson.split(",").filter { it.isNotBlank() }
        set(value) { goalsJson = value.joinToString(",") }

    fun toDto(): com.mealmanager.backend.model.dto.DietaryPreferencesDto {
        return com.mealmanager.backend.model.dto.DietaryPreferencesDto(
            userId = userId,
            restrictions = restrictions,
            preferences = preferences,
            goals = goals,
            timezone = timezone
        )
    }
}

class AuditLogEntity(
    override val id: UUID = UUID.randomUUID(),
    var userId: UUID = UUID.randomUUID(),
    var action: String = "",
    var details: String = "",
    var timestamp: Instant = Instant.now()
) : BaseEntity(id, createdAt = timestamp, updatedAt = timestamp) {

    fun toDto(): com.mealmanager.backend.model.dto.AuditLogDto {
        return com.mealmanager.backend.model.dto.AuditLogDto(
            id = id,
            userId = userId.toString(),
            action = action,
            details = details,
            timestamp = timestamp.toString()
        )
    }
}

class ShareEntity(
    override val id: UUID = UUID.randomUUID(),
    var planId: UUID = UUID.randomUUID(),
    var sharedWithJson: String = "",
    var permissionsJson: String = "",
    var token: String = "",
    var expiresAt: Instant = Instant.now().plusSeconds(86400 * 7),
    override var updatedAt: Instant = Instant.now()
) : BaseEntity(id, createdAt = Instant.now(), updatedAt = updatedAt) {

    var sharedWith: List<String>
        get() = sharedWithJson.split(",").filter { it.isNotBlank() }
        set(value) { sharedWithJson = value.joinToString(",") }

    var permissions: List<String>
        get() = permissionsJson.split(",").filter { it.isNotBlank() }
        set(value) { permissionsJson = value.joinToString(",") }

    fun toDto(): com.mealmanager.backend.model.dto.ShareDto {
        return com.mealmanager.backend.model.dto.ShareDto(
            shareId = id,
            planId = planId,
            sharedWith = sharedWith,
            permissions = permissions,
            createdAt = createdAt.toString()
        )
    }
}

class CollectionEntity(
    override val id: UUID = UUID.randomUUID(),
    var userId: String = "",
    var name: String = "",
    var recipeIdsJson: String = "",
    var isPublic: Boolean = false,
    override var updatedAt: Instant = Instant.now()
) : BaseEntity(id, createdAt = Instant.now(), updatedAt = updatedAt) {

    var recipeIds: List<UUID>
        get() = recipeIdsJson.split(",").filter { it.isNotBlank() }.map { UUID.fromString(it) }
        set(value) { recipeIdsJson = value.joinToString(",") }

    fun toSummaryDto(): com.mealmanager.backend.model.dto.RecipeSummaryDto {
        return com.mealmanager.backend.model.dto.RecipeSummaryDto(
            id = id,
            title = name,
            thumbnailUrl = null
        )
    }
}

class RatingEntity(
    override val id: UUID = UUID.randomUUID(),
    var recipeId: UUID = UUID.randomUUID(),
    var userId: String = "",
    var rating: Int = 5,
    var review: String = "",
    override var updatedAt: Instant = Instant.now()
) : BaseEntity(id, createdAt = Instant.now(), updatedAt = updatedAt) {

    fun toDto(): com.mealmanager.backend.model.dto.RatingDto {
        return com.mealmanager.backend.model.dto.RatingDto(
            id = id,
            recipeId = recipeId,
            userId = userId,
            rating = rating,
            review = review,
            createdAt = createdAt.toString()
        )
    }
}

class PackagedFoodEntity(
    override val id: UUID = UUID.randomUUID(),
    var barcode: String = "",
    var name: String = "",
    var brand: String = "",
    var nutritionJson: String = "",
    var ingredientsJson: String = "",
    var servingSize: String = "",
    override var updatedAt: Instant = Instant.now()
) : BaseEntity(id, createdAt = Instant.now(), updatedAt = updatedAt) {

    var nutrition: com.mealmanager.backend.model.dto.NutritionInfoDto
        get() = com.mealmanager.backend.model.dto.NutritionInfoDto(
            calories = nutritionJson.split(",").getOrElse(0) { "0" }.toDoubleOrNull() ?: 0.0,
            protein = nutritionJson.split(",").getOrElse(1) { "0" }.toDoubleOrNull() ?: 0.0,
            carbs = nutritionJson.split(",").getOrElse(2) { "0" }.toDoubleOrNull() ?: 0.0,
            fat = nutritionJson.split(",").getOrElse(3) { "0" }.toDoubleOrNull() ?: 0.0
        )
        set(value) {
            nutritionJson = "${value.calories},${value.protein},${value.carbs},${value.fat}"
        }

    var ingredients: List<String>
        get() = ingredientsJson.split(",").filter { it.isNotBlank() }
        set(value) { ingredientsJson = value.joinToString(",") }

    fun toDto(): com.mealmanager.backend.model.dto.PackagedFoodDto {
        return com.mealmanager.backend.model.dto.PackagedFoodDto(
            id = id,
            barcode = barcode,
            name = name,
            brand = brand,
            nutrition = nutrition,
            ingredients = ingredients,
            servingSize = servingSize
        )
    }
}

class FridgeItemEntity(
    override val id: UUID = UUID.randomUUID(),
    var name: String = "",
    var quantity: Double = 0.0,
    var unit: String = "",
    var category: String = "",
    var expirationDate: String? = null,
    var lowStockThreshold: Int = 1,
    override var updatedAt: Instant = Instant.now()
) : BaseEntity(id, createdAt = Instant.now(), updatedAt = updatedAt) {

    fun toDto(): com.mealmanager.backend.model.dto.FridgeItemDto {
        return com.mealmanager.backend.model.dto.FridgeItemDto(
            id = id,
            name = name,
            quantity = quantity,
            unit = unit,
            category = category,
            expirationDate = expirationDate,
            lowStockThreshold = lowStockThreshold,
            createdAt = createdAt.toString()
        )
    }
}

class PantryItemEntity(
    override val id: UUID = UUID.randomUUID(),
    var name: String = "",
    var quantity: Double = 0.0,
    var unit: String = "",
    var category: String = "",
    var reorderThreshold: Int = 1,
    var estimatedReorderCost: Double = 0.0,
    override var updatedAt: Instant = Instant.now()
) : BaseEntity(id, createdAt = Instant.now(), updatedAt = updatedAt) {

    fun toDto(): com.mealmanager.backend.model.dto.PantryItemDto {
        return com.mealmanager.backend.model.dto.PantryItemDto(
            id = id,
            name = name,
            quantity = quantity,
            unit = unit,
            category = category,
            reorderThreshold = reorderThreshold,
            estimatedReorderCost = estimatedReorderCost,
            createdAt = createdAt.toString()
        )
    }

    fun toThresholdDto(): com.mealmanager.backend.model.dto.ReorderThresholdDto {
        return com.mealmanager.backend.model.dto.ReorderThresholdDto(
            itemId = id,
            itemName = name,
            threshold = reorderThreshold,
            currentQuantity = quantity.toInt()
        )
    }
}

class WeightLogEntity(
    override val id: UUID = UUID.randomUUID(),
    var userId: String = "",
    var date: String = "",
    var weightKg: Double = 0.0,
    var bodyFatPercent: Double? = null,
    override var updatedAt: Instant = Instant.now()
) : BaseEntity(id, createdAt = Instant.now(), updatedAt = updatedAt) {

    fun toDto(): com.mealmanager.backend.model.dto.WeightLogDto {
        return com.mealmanager.backend.model.dto.WeightLogDto(
            id = id,
            userId = userId,
            date = date,
            weightKg = weightKg,
            bodyFatPercent = bodyFatPercent,
            createdAt = createdAt.toString()
        )
    }
}

class WaterLogEntity(
    override val id: UUID = UUID.randomUUID(),
    var userId: String = "",
    var date: String = "",
    var amountMl: Double = 0.0,
    override var updatedAt: Instant = Instant.now()
) : BaseEntity(id, createdAt = Instant.now(), updatedAt = updatedAt) {

    fun toDto(): com.mealmanager.backend.model.dto.WaterLogDto {
        return com.mealmanager.backend.model.dto.WaterLogDto(
            id = id,
            userId = userId,
            date = date,
            amountMl = amountMl,
            createdAt = createdAt.toString()
        )
    }
}

class FastingWindowEntity(
    override val id: UUID = UUID.randomUUID(),
    var userId: String = "",
    var startTime: String? = null,
    var endTime: String? = null,
    var durationHours: Double = 0.0,
    var completed: Boolean = false,
    override var updatedAt: Instant = Instant.now()
) : BaseEntity(id, createdAt = Instant.now(), updatedAt = updatedAt) {

    fun toDto(): com.mealmanager.backend.model.dto.FastingWindowDto {
        return com.mealmanager.backend.model.dto.FastingWindowDto(
            id = id,
            userId = userId,
            startTime = startTime,
            endTime = endTime,
            durationHours = durationHours,
            completed = completed,
            createdAt = createdAt.toString()
        )
    }

    fun toHistoryDto(): com.mealmanager.backend.service.FastingHistoryDto {
        return com.mealmanager.backend.service.FastingHistoryDto(
            date = startTime ?: "",
            durationHours = durationHours,
            completed = completed
        )
    }
}
