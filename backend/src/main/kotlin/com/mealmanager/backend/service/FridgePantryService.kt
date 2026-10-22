package com.mealmanager.backend.service

import com.mealmanager.backend.model.dto.FridgeItemDto
import com.mealmanager.backend.model.dto.PantryItemDto
import com.mealmanager.backend.model.dto.ReorderThresholdDto
import com.mealmanager.backend.model.dto.GrocerySuggestionDto
import com.mealmanager.backend.repository.FridgeRepository
import com.mealmanager.backend.repository.PantryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional
class FridgeService(
    private val fridgeRepository: FridgeRepository
) {

    fun listFridgeItems(): List<FridgeItemDto> {
        return fridgeRepository.findAll().map { it.toDto() }
    }

    fun addFridgeItem(item: FridgeItemDto): FridgeItemDto {
        val entity = item.toEntity()
        val saved = fridgeRepository.save(entity)
        return saved.toDto()
    }

    fun updateFridgeItem(id: UUID, item: FridgeItemDto): FridgeItemDto {
        val entity = fridgeRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Fridge item not found: $id") }
        entity.updateFromDto(item)
        val saved = fridgeRepository.save(entity)
        return saved.toDto()
    }

    fun deleteFridgeItem(id: UUID) {
        if (!fridgeRepository.existsById(id)) {
            throw IllegalArgumentException("Fridge item not found: $id")
        }
        fridgeRepository.deleteById(id)
    }

    fun getExpiringItems(days: Int): List<FridgeItemDto> {
        val cutoff = java.time.LocalDate.now().plusDays(days)
        return fridgeRepository.findExpiringBefore(cutoff).map { it.toDto() }
    }

    fun getLowStockItems(): List<FridgeItemDto> {
        return fridgeRepository.findLowStock().map { it.toDto() }
    }
}

@Service
@Transactional
class PantryService(
    private val pantryRepository: PantryRepository,
    private val groceryService: com.mealmanager.backend.service.GroceryService
) {

    fun listPantryItems(): List<PantryItemDto> {
        return pantryRepository.findAll().map { it.toDto() }
    }

    fun addPantryItem(item: PantryItemDto): PantryItemDto {
        val entity = item.toEntity()
        val saved = pantryRepository.save(entity)
        return saved.toDto()
    }

    fun updatePantryItem(id: UUID, item: PantryItemDto): PantryItemDto {
        val entity = pantryRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Pantry item not found: $id") }
        entity.updateFromDto(item)
        val saved = pantryRepository.save(entity)
        return saved.toDto()
    }

    fun deletePantryItem(id: UUID) {
        if (!pantryRepository.existsById(id)) {
            throw IllegalArgumentException("Pantry item not found: $id")
        }
        pantryRepository.deleteById(id)
    }

    fun getReorderThresholds(): List<ReorderThresholdDto> {
        return pantryRepository.findBelowThreshold().map { it.toThresholdDto() }
    }

    fun autoAddToGrocery(): GrocerySuggestionDto {
        val belowThreshold = pantryRepository.findBelowThreshold()
        val items = belowThreshold.map { it.name }
        val estimatedCost = belowThreshold.sumOf { it.estimatedReorderCost }
        return GrocerySuggestionDto(items = items, totalEstimatedCost = estimatedCost)
    }
}
