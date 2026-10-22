package com.mealmanager.backend.controller

import com.mealmanager.backend.model.dto.ApiResponse
import com.mealmanager.backend.model.dto.SearchResultDto
import com.mealmanager.backend.service.SearchService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/search")
class SearchController(
    private val searchService: SearchService
) {

    @GetMapping
    fun search(
        @RequestParam q: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<ApiResponse<Page<SearchResultDto>>> {
        val result = searchService.search(q, page, size)
        return ResponseEntity.ok(ApiResponse.success(result))
    }

    @GetMapping("/recipes")
    fun searchRecipes(
        @RequestParam q: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<ApiResponse<Page<SearchResultDto>>> {
        val result = searchService.searchRecipes(q, page, size)
        return ResponseEntity.ok(ApiResponse.success(result))
    }

    @GetMapping("/suggestions")
    fun getSuggestions(@RequestParam q: String): ResponseEntity<ApiResponse<List<String>>> {
        val suggestions = searchService.getSuggestions(q)
        return ResponseEntity.ok(ApiResponse.success(suggestions))
    }
}
