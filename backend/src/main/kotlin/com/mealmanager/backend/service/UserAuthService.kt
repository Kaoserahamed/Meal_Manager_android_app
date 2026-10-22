package com.mealmanager.backend.service

import com.mealmanager.backend.model.dto.UserDto
import com.mealmanager.backend.model.dto.ProfileDto
import com.mealmanager.backend.model.dto.DietaryPreferencesDto
import com.mealmanager.backend.model.dto.AuthResponse
import com.mealmanager.backend.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional
class UserService(
    private val userRepository: UserRepository
) {

    fun listUsers(page: Int, size: Int): com.mealmanager.backend.model.dto.Page<UserDto> {
        val users = userRepository.findAll(page, size)
        return com.mealmanager.backend.model.dto.Page(
            content = users.map { it.toDto() },
            page = page,
            size = size,
            total = userRepository.count()
        )
    }

    fun getUserById(id: UUID): UserDto {
        return userRepository.findById(id)
            .orElseThrow { IllegalArgumentException("User not found: $id") }
            .toDto()
    }

    fun updateUser(id: UUID, dto: UserDto): UserDto {
        val entity = userRepository.findById(id)
            .orElseThrow { IllegalArgumentException("User not found: $id") }
        entity.updateFromDto(dto)
        val saved = userRepository.save(entity)
        return saved.toDto()
    }

    fun deleteUser(id: UUID) {
        if (!userRepository.existsById(id)) {
            throw IllegalArgumentException("User not found: $id")
        }
        userRepository.deleteById(id)
    }

    fun getProfile(id: UUID): ProfileDto {
        return userRepository.findProfileByUserId(id)
            .map { it.toDto() }
            .orElseGet { ProfileDto.default(id) }
    }

    fun updateProfile(id: UUID, dto: ProfileDto): ProfileDto {
        val entity = dto.toEntity(id)
        val saved = userRepository.saveProfile(entity)
        return saved.toDto()
    }

    fun getDietaryPreferences(id: UUID): DietaryPreferencesDto {
        return userRepository.findDietaryPreferences(id)
            .map { it.toDto() }
            .orElseGet { DietaryPreferencesDto.default(id) }
    }

    fun updateDietaryPreferences(id: UUID, dto: DietaryPreferencesDto): DietaryPreferencesDto {
        val entity = dto.toEntity(id)
        val saved = userRepository.saveDietaryPreferences(entity)
        return saved.toDto()
    }
}

@Service
@Transactional
class AuthService(
    private val userRepository: UserRepository
) {

    fun login(request: com.mealmanager.backend.controller.LoginRequest): AuthResponse {
        val user = userRepository.findByEmail(request.email)
            .orElseThrow { IllegalArgumentException("Invalid credentials") }
        if (!user.passwordMatches(request.password)) {
            throw IllegalArgumentException("Invalid credentials")
        }
        return AuthResponse(
            accessToken = generateToken(user.id),
            refreshToken = generateRefreshToken(user.id),
            userId = user.id,
            expiresIn = 3600L
        )
    }

    fun register(request: com.mealmanager.backend.controller.RegisterRequest): AuthResponse {
        if (userRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("Email already registered")
        }
        val user = com.mealmanager.backend.model.entity.UserEntity(
            id = UUID.randomUUID(),
            email = request.email,
            passwordHash = hashPassword(request.password),
            name = request.name,
            role = request.role,
            createdAt = java.time.Instant.now()
        )
        val saved = userRepository.save(user)
        return AuthResponse(
            accessToken = generateToken(saved.id),
            refreshToken = generateRefreshToken(saved.id),
            userId = saved.id,
            expiresIn = 3600L
        )
    }

    fun logout(token: String) {
        userRepository.revokeToken(token)
    }

    fun refreshToken(request: com.mealmanager.backend.controller.RefreshTokenRequest): AuthResponse {
        val userId = userRepository.validateRefreshToken(request.refreshToken)
            .orElseThrow { IllegalArgumentException("Invalid refresh token") }
        return AuthResponse(
            accessToken = generateToken(userId),
            refreshToken = generateRefreshToken(userId),
            userId = userId,
            expiresIn = 3600L
        )
    }

    fun biometricAuth(request: com.mealmanager.backend.controller.BiometricAuthRequest): AuthResponse {
        if (!userRepository.validateBiometric(request.userId, request.biometricToken)) {
            throw IllegalArgumentException("Biometric authentication failed")
        }
        return AuthResponse(
            accessToken = generateToken(request.userId),
            refreshToken = generateRefreshToken(request.userId),
            userId = request.userId,
            expiresIn = 3600L
        )
    }

    fun familySso(request: com.mealmanager.backend.controller.FamilySsoRequest): com.mealmanager.backend.controller.FamilySsoResponse {
        val permissions = if (request.parentalControl) listOf("read", "write", "manage_children") else listOf("read", "write")
        return com.mealmanager.backend.controller.FamilySsoResponse(
            accessToken = generateToken(request.userId),
            familyId = request.familyId,
            permissions = permissions
        )
    }

    private fun generateToken(userId: UUID): String {
        return "access-token-${userId}-${System.currentTimeMillis()}"
    }

    private fun generateRefreshToken(userId: UUID): String {
        return "refresh-token-${userId}-${System.currentTimeMillis()}"
    }

    private fun hashPassword(password: String): String {
        return "hashed:$password"
    }
}
