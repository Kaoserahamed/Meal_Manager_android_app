package com.mealmanager.backend.controller

import com.mealmanager.backend.model.dto.UserDto
import com.mealmanager.backend.model.dto.ProfileDto
import com.mealmanager.backend.model.dto.ApiResponse
import com.mealmanager.backend.service.UserService
import com.mealmanager.backend.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid
import java.util.UUID

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService
) {

    @GetMapping
    fun listUsers(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<ApiResponse<Page<UserDto>>> {
        val result = userService.listUsers(page, size)
        return ResponseEntity.ok(ApiResponse.success(result))
    }

    @GetMapping("/{id}")
    fun getUser(@PathVariable id: UUID): ResponseEntity<ApiResponse<UserDto>> {
        val user = userService.getUserById(id)
        return ResponseEntity.ok(ApiResponse.success(user))
    }

    @PutMapping("/{id}")
    fun updateUser(
        @PathVariable id: UUID,
        @Valid @RequestBody user: UserDto
    ): ResponseEntity<ApiResponse<UserDto>> {
        val updated = userService.updateUser(id, user)
        return ResponseEntity.ok(ApiResponse.success(updated))
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: UUID): ResponseEntity<ApiResponse<Void>> {
        userService.deleteUser(id)
        return ResponseEntity.ok(ApiResponse.success(null))
    }

    @GetMapping("/{id}/profile")
    fun getProfile(@PathVariable id: UUID): ResponseEntity<ApiResponse<ProfileDto>> {
        val profile = userService.getProfile(id)
        return ResponseEntity.ok(ApiResponse.success(profile))
    }

    @PutMapping("/{id}/profile")
    fun updateProfile(
        @PathVariable id: UUID,
        @Valid @RequestBody profile: ProfileDto
    ): ResponseEntity<ApiResponse<ProfileDto>> {
        val updated = userService.updateProfile(id, profile)
        return ResponseEntity.ok(ApiResponse.success(updated))
    }

    @GetMapping("/{id}/dietary-preferences")
    fun getDietaryPreferences(@PathVariable id: UUID): ResponseEntity<ApiResponse<DietaryPreferencesDto>> {
        val prefs = userService.getDietaryPreferences(id)
        return ResponseEntity.ok(ApiResponse.success(prefs))
    }

    @PutMapping("/{id}/dietary-preferences")
    fun updateDietaryPreferences(
        @PathVariable id: UUID,
        @Valid @RequestBody prefs: DietaryPreferencesDto
    ): ResponseEntity<ApiResponse<DietaryPreferencesDto>> {
        val updated = userService.updateDietaryPreferences(id, prefs)
        return ResponseEntity.ok(ApiResponse.success(updated))
    }
}

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<ApiResponse<AuthResponse>> {
        val response = authService.login(request)
        return ResponseEntity.ok(ApiResponse.success(response))
    }

    @PostMapping("/register")
    fun register(@Valid @RequestBody request: RegisterRequest): ResponseEntity<ApiResponse<AuthResponse>> {
        val response = authService.register(request)
        return ResponseEntity.ok(ApiResponse.success(response))
    }

    @PostMapping("/logout")
    fun logout(@RequestHeader("Authorization") token: String): ResponseEntity<ApiResponse<Void>> {
        authService.logout(token)
        return ResponseEntity.ok(ApiResponse.success(null))
    }

    @PostMapping("/refresh")
    fun refreshToken(@Valid @RequestBody request: RefreshTokenRequest): ResponseEntity<ApiResponse<AuthResponse>> {
        val response = authService.refreshToken(request)
        return ResponseEntity.ok(ApiResponse.success(response))
    }

    @PostMapping("/biometric")
    fun biometricAuth(@Valid @RequestBody request: BiometricAuthRequest): ResponseEntity<ApiResponse<AuthResponse>> {
        val response = authService.biometricAuth(request)
        return ResponseEntity.ok(ApiResponse.success(response))
    }

    @PostMapping("/sso/family")
    fun familySso(@Valid @RequestBody request: FamilySsoRequest): ResponseEntity<ApiResponse<FamilySsoResponse>> {
        val response = authService.familySso(request)
        return ResponseEntity.ok(ApiResponse.success(response))
    }
}

data class LoginRequest(val email: String, val password: String)
data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String,
    val role: String
)
data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val userId: UUID,
    val expiresIn: Long
)
data class RefreshTokenRequest(val refreshToken: String)
data class BiometricAuthRequest(
    val userId: UUID,
    val biometricToken: String
)
data class FamilySsoRequest(
    val familyId: UUID,
    val userId: UUID,
    val parentalControl: Boolean
)
data class FamilySsoResponse(
    val accessToken: String,
    val familyId: UUID,
    val permissions: List<String>
)

data class DietaryPreferencesDto(
    val userId: UUID,
    val restrictions: List<String>,
    val preferences: List<String>,
    val goals: List<String>,
    val timezone: String
)
