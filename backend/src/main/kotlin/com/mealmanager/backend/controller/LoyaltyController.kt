package com.mealmanager.backend.controller

import com.mealmanager.backend.model.dto.ApiResponse
import com.mealmanager.backend.model.dto.LoyaltyAccountDto
import com.mealmanager.backend.model.dto.RewardDto
import com.mealmanager.backend.service.LoyaltyService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import jakarta.validation.Valid
import java.util.UUID

@RestController
@RequestMapping("/api/v1/loyalty")
class LoyaltyController(
    private val loyaltyService: LoyaltyService
) {

    @GetMapping("/{userId}/account")
    fun getAccount(@PathVariable userId: UUID): ResponseEntity<ApiResponse<LoyaltyAccountDto>> {
        val account = loyaltyService.getAccount(userId)
        return ResponseEntity.ok(ApiResponse.success(account))
    }

    @PostMapping("/points")
    fun addPoints(@Valid @RequestBody request: AddPointsRequest): ResponseEntity<ApiResponse<LoyaltyAccountDto>> {
        val account = loyaltyService.addPoints(request)
        return ResponseEntity.ok(ApiResponse.success(account))
    }

    @GetMapping("/rewards")
    fun listRewards(): ResponseEntity<ApiResponse<List<RewardDto>>> {
        val rewards = loyaltyService.listRewards()
        return ResponseEntity.ok(ApiResponse.success(rewards))
    }

    @PostMapping("/rewards/redeem")
    fun redeemReward(@Valid @RequestBody request: RedeemRewardRequest): ResponseEntity<ApiResponse<Void>> {
        loyaltyService.redeemReward(request)
        return ResponseEntity.ok(ApiResponse.success(null))
    }
}

data class AddPointsRequest(
    val userId: UUID = UUID.randomUUID(),
    val points: Int = 0,
    val reason: String = ""
)
data class RedeemRewardRequest(
    val userId: UUID = UUID.randomUUID(),
    val rewardId: UUID = UUID.randomUUID()
)
