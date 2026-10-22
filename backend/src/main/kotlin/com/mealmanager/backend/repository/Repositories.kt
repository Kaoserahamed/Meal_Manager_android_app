package com.mealmanager.backend.repository

import com.mealmanager.backend.model.entity.*
import org.springframework.stereotype.Repository
import java.util.*
import java.time.LocalDate

@Repository
class RecipeRepository {
    private val recipes = mutableMapOf<UUID, RecipeEntity>()

    fun findAll(tag: String?, dietary: String?, page: Int, size: Int): List<RecipeEntity> {
        return recipes.values
            .filter { tag == null || it.tags.contains(tag) }
            .filter { dietary == null || it.tags.contains(dietary) }
            .subList(page * size, Math.min((page + 1) * size, recipes.size))
    }

    fun count(tag: String?, dietary: String?): Long {
        return recipes.values.count { (tag == null || it.tags.contains(tag)) && (dietary == null || it.tags.contains(dietary)) }
    }

    fun findById(id: UUID): Optional<RecipeEntity> = Optional.ofNullable(recipes[id])

    fun save(entity: RecipeEntity): RecipeEntity {
        recipes[entity.id] = entity
        return entity
    }

    fun existsById(id: UUID): Boolean = recipes.containsKey(id)

    fun deleteById(id: UUID) { recipes.remove(id) }

    fun shareRecipe(id: UUID, token: String) {
        recipes[id]?.let { recipes[id] = it }
    }

    fun saveMealPhoto(entity: com.mealmanager.backend.model.entity.RecipeEntity): com.mealmanager.backend.model.entity.RecipeEntity {
        return save(entity)
    }

    fun findByIngredient(ingredient: String, page: Int, size: Int): com.mealmanager.backend.model.dto.Page<RecipeEntity> {
        val list = recipes.values.filter { it.ingredients.contains(ingredient) }.toList()
        return com.mealmanager.backend.model.dto.Page(
            content = list.subList(page * size, Math.min((page + 1) * size, list.size)),
            page = page, size = size, total = list.size
        )
    }
}

@Repository
class MealPlanRepository {
    private val plans = mutableMapOf<UUID, MealPlanEntity>()
    private val templates = mutableMapOf<UUID, MealPlanTemplateEntity>()

    fun findAll(page: Int, size: Int): List<MealPlanEntity> {
        return plans.values.subList(page * size, Math.min((page + 1) * size, plans.size))
    }

    fun count(): Long = plans.size

    fun findById(id: UUID): Optional<MealPlanEntity> = Optional.ofNullable(plans[id])

    fun save(entity: MealPlanEntity): MealPlanEntity {
        plans[entity.id] = entity
        return entity
    }

    fun existsById(id: UUID): Boolean = plans.containsKey(id)

    fun deleteById(id: UUID) { plans.remove(id) }

    fun saveTemplate(entity: MealPlanTemplateEntity): MealPlanTemplateEntity {
        templates[entity.id] = entity
        return entity
    }

    fun findAllTemplates(): List<MealPlanTemplateEntity> = templates.values.toList()
}

@Repository
class NutritionRepository {
    private val logs = mutableMapOf<UUID, NutritionLogEntity>()
    private var goals: NutritionGoalEntity? = null

    fun findByDateRange(startDate: LocalDate, endDate: LocalDate): List<NutritionLogEntity> {
        return logs.values.filter { it.date >= startDate.toString() && it.date <= endDate.toString() }
    }

    fun save(entity: NutritionLogEntity): NutritionLogEntity {
        logs[entity.id] = entity
        return entity
    }

    fun findGoals(): Optional<NutritionGoalEntity> = Optional.ofNullable(goals)

    fun saveGoals(entity: NutritionGoalEntity): NutritionGoalEntity {
        goals = entity
        return entity
    }

    fun findByDate(date: LocalDate): List<NutritionLogEntity> {
        return logs.values.filter { it.date == date.toString() }
    }

    fun findTrends(range: String): List<NutritionLogEntity> = logs.values.toList()
}

@Repository
class GroceryRepository {
    private val lists = mutableMapOf<UUID, GroceryListEntity>()

    fun findAll(page: Int, size: Int): List<GroceryListEntity> {
        return lists.values.subList(page * size, Math.min((page + 1) * size, lists.size))
    }

    fun count(): Long = lists.size

    fun findById(id: UUID): Optional<GroceryListEntity> = Optional.ofNullable(lists[id])

    fun save(entity: GroceryListEntity): GroceryListEntity {
        lists[entity.id] = entity
        return entity
    }

    fun existsById(id: UUID): Boolean = lists.containsKey(id)

    fun deleteById(id: UUID) { lists.remove(id) }
}

@Repository
class SocialRepository {
    private val shares = mutableMapOf<UUID, ShareEntity>()
    private val collections = mutableMapOf<UUID, CollectionEntity>()
    private val ratings = mutableMapOf<UUID, RatingEntity>()

    fun saveShare(entity: ShareEntity): ShareEntity {
        shares[entity.id] = entity
        return entity
    }

    fun findShareById(id: UUID): Optional<ShareEntity> = Optional.ofNullable(shares[id])

    fun saveCollection(entity: CollectionEntity): CollectionEntity {
        collections[entity.id] = entity
        return entity
    }

    fun findAllCollections(): List<CollectionEntity> = collections.values.toList()

    fun findCollectionRecipes(id: UUID): List<RecipeEntity> = emptyList()

    fun saveRating(entity: RatingEntity): RatingEntity {
        ratings[entity.id] = entity
        return entity
    }

    fun findRatingsByRecipe(recipeId: UUID): List<RatingEntity> =
        ratings.values.filter { it.recipeId == recipeId }.toList()
}

@Repository
class BarcodeRepository {
    private val foods = mutableMapOf<String, PackagedFoodEntity>()
    private val nutritionLogs = mutableMapOf<UUID, NutritionLogEntity>()

    fun findByBarcode(barcode: String): Optional<PackagedFoodEntity> = Optional.ofNullable(foods[barcode])

    fun save(entity: PackagedFoodEntity): PackagedFoodEntity {
        foods[entity.barcode] = entity
        return entity
    }

    fun search(query: String, page: Int, size: Int): List<PackagedFoodEntity> {
        return foods.values.filter { it.name.contains(query, ignoreCase = true) }.toList()
    }

    fun count(query: String): Long = foods.values.count { it.name.contains(query, ignoreCase = true) }

    fun saveNutritionLog(entity: NutritionLogEntity) {
        nutritionLogs[entity.id] = entity
    }
}

@Repository
class FridgeRepository {
    private val items = mutableMapOf<UUID, FridgeItemEntity>()
    fun findAll(): List<FridgeItemEntity> = items.values.toList()
    fun save(entity: FridgeItemEntity): FridgeItemEntity { items[entity.id] = entity; return entity }
    fun findById(id: UUID): Optional<FridgeItemEntity> = Optional.ofNullable(items[id])
    fun existsById(id: UUID): Boolean = items.containsKey(id)
    fun deleteById(id: UUID) { items.remove(id) }
    fun findExpiringBefore(cutoff: LocalDate): List<FridgeItemEntity> =
        items.values.filter { it.expirationDate != null && it.expirationDate < cutoff.toString() }.toList()
    fun findLowStock(): List<FridgeItemEntity> = emptyList()
}

@Repository
class PantryRepository {
    private val items = mutableMapOf<UUID, PantryItemEntity>()
    fun findAll(): List<PantryItemEntity> = items.values.toList()
    fun save(entity: PantryItemEntity): PantryItemEntity { items[entity.id] = entity; return entity }
    fun findById(id: UUID): Optional<PantryItemEntity> = Optional.ofNullable(items[id])
    fun existsById(id: UUID): Boolean = items.containsKey(id)
    fun deleteById(id: UUID) { items.remove(id) }
    fun findBelowThreshold(): List<PantryItemEntity> = items.values.filter { it.quantity < it.reorderThreshold }.toList()
}

@Repository
class UserRepository {
    private val users = mutableMapOf<UUID, UserEntity>()
    private val profiles = mutableMapOf<String, ProfileEntity>()
    private val dietaryPrefs = mutableMapOf<String, DietaryPreferencesEntity>()

    fun findAll(page: Int, size: Int): List<UserEntity> = users.values.subList(page * size, Math.min((page + 1) * size, users.size))
    fun count(): Long = users.size
    fun findById(id: UUID): Optional<UserEntity> = Optional.ofNullable(users[id])
    fun save(entity: UserEntity): UserEntity { users[entity.id] = entity; return entity }
    fun existsById(id: UUID): Boolean = users.containsKey(id)
    fun deleteById(id: UUID) { users.remove(id) }
    fun findByEmail(email: String): Optional<UserEntity> = users.values.find { it.email == email }.map { Optional.of(it) }.orElse(Optional.empty())
    fun existsByEmail(email: String): Boolean = users.values.any { it.email == email }
    fun revokeToken(token: String) {}
    fun validateRefreshToken(token: String): Optional<UUID> = Optional.empty()
    fun validateBiometric(userId: UUID, token: String): Boolean = true
    fun findProfileByUserId(userId: UUID): Optional<ProfileEntity> = Optional.ofNullable(profiles[userId.toString()])
    fun saveProfile(entity: ProfileEntity): ProfileEntity { profiles[entity.userId] = entity; return entity }
    fun findDietaryPreferences(userId: UUID): Optional<DietaryPreferencesEntity> = Optional.ofNullable(dietaryPrefs[userId.toString()])
    fun saveDietaryPreferences(entity: DietaryPreferencesEntity): DietaryPreferencesEntity { dietaryPrefs[entity.userId] = entity; return entity }
}

@Repository
class AuditRepository {
    private val logs = mutableMapOf<UUID, AuditLogEntity>()
    fun save(entity: AuditLogEntity) { logs[entity.id] = entity }
    fun findAll(action: String?, page: Int, size: Int): List<AuditLogEntity> =
        logs.values.filter { action == null || it.action == action }.subList(page * size, Math.min((page + 1) * size, logs.size))
    fun count(action: String?): Long = logs.values.count { action == null || it.action == action }
    fun recordExport(url: String) {}
}

@Repository
class GdprRepository {
    fun createExportRequest(userId: UUID, exportId: UUID) {}
    fun createDeletionRequest(userId: UUID, deletionId: UUID) {}
    fun updateConsent(userId: UUID, consentType: String, granted: Boolean) {}
}

@Repository
class FeatureFlagRepository {
    private val flags = mutableMapOf<String, FeatureFlagEntity>()
    fun findAll(): List<FeatureFlagEntity> = flags.values.toList()
    fun save(entity: FeatureFlagEntity): FeatureFlagEntity { flags[entity.key] = entity; return entity }
    fun findByKey(key: String): Optional<FeatureFlagEntity> = Optional.ofNullable(flags[key])
}

class FeatureFlagEntity(
    val key: String = "",
    var enabled: Boolean = false,
    var description: String = ""
) {
    fun toDto(): com.mealmanager.backend.model.dto.FeatureFlagDto =
        com.mealmanager.backend.model.dto.FeatureFlagDto(key = key, enabled = enabled, description = description)
}

@Repository
class AbTestRepository {
    fun findAll(): List<com.mealmanager.backend.model.entity.AbTestEntity> = emptyList()
    fun recordAssignment(experimentId: UUID, userId: UUID, variant: String) {}
    fun recordResult(experimentId: UUID, userId: UUID, variant: String, converted: Boolean) {}
}

class AbTestEntity(
    val id: UUID = UUID.randomUUID(),
    var name: String = "",
    var status: String = "draft"
)

class WeightRepository {
    private val logs = mutableMapOf<UUID, WeightLogEntity>()
    fun findAll(): List<WeightLogEntity> = logs.values.toList()
    fun save(entity: WeightLogEntity): WeightLogEntity { logs[entity.id] = entity; return entity }
    fun findLatest(): WeightLogEntity? = logs.values.maxByOrNull { it.createdAt.toEpochMilli() }
}

class WaterRepository {
    private val logs = mutableMapOf<UUID, WaterLogEntity>()
    fun findAll(): List<WaterLogEntity> = logs.values.toList()
    fun save(entity: WaterLogEntity): WaterLogEntity { logs[entity.id] = entity; return entity }
    fun findByDate(date: LocalDate): List<WaterLogEntity> = logs.values.filter { it.date == date.toString() }.toList()
    fun setReminder(userId: UUID, intervalMinutes: Int, enabled: Boolean) {}
}

class FastingRepository {
    private val windows = mutableMapOf<UUID, FastingWindowEntity>()
    fun findAll(): List<FastingWindowEntity> = windows.values.toList()
    fun save(entity: FastingWindowEntity): FastingWindowEntity { windows[entity.id] = entity; return entity }
    fun findById(id: UUID): Optional<FastingWindowEntity> = Optional.ofNullable(windows[id])
}
