package com.mealmanager.backend.service

import com.mealmanager.backend.model.dto.LoyaltyAccountDto
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class LoyaltyService {

    fun getAccount(userId: UUID): LoyaltyAccountDto {
        return LoyaltyAccountDto(userId = userId, points = 500, tier = "silver")
    }

    fun addPoints(request: com.mealmanager.backend.controller.AddPointsRequest): LoyaltyAccountDto {
        return LoyaltyAccountDto(userId = request.userId, points = request.points, tier = "gold")
    }

    fun listRewards(): List<com.mealmanager.backend.model.dto.RewardDto> {
        return emptyList()
    }

    fun redeemReward(request: com.mealmanager.backend.controller.RedeemRewardRequest) {
        // Redeem reward
    }
}
