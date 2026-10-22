package com.mealmanager.backend.service

import com.mealmanager.backend.model.dto.Page
import com.mealmanager.backend.model.dto.SearchResultDto
import org.springframework.stereotype.Service

@Service
class SearchService {

    fun search(q: String, page: Int, size: Int): Page<SearchResultDto> {
        return Page(content = emptyList(), page = page, size = size, total = 0)
    }

    fun searchRecipes(q: String, page: Int, size: Int): Page<SearchResultDto> {
        return Page(content = emptyList(), page = page, size = size, total = 0)
    }

    fun getSuggestions(q: String): List<String> {
        return listOf("pasta", "salad", "soup")
    }
}
