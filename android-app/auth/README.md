# Meal_Manager_android_app Authentication Module

## Overview
Authentication system for user accounts with OAuth2, JWT, and biometric auth.

## Files
- `android-app/auth/AuthManager.kt` - Authentication logic
- `android-app/auth/UserModel.kt` - User model
- `android-app/auth/BiometricAuth.kt` - Biometric authentication
- `android-app/auth/AuthInterceptor.kt` - Auth interceptor for API calls

## Features
- User account registration/login
- JWT-based session management
- OAuth2 provider integration
- Biometric authentication (fingerprint/face ID)
- Password hashing with bcrypt
- Session timeout handling
- Secure token storage (EncryptedSharedPreferences)

## Tech Stack
- Kotlin/Android
- Jetpack Compose
- Firebase Auth
- JWT
- BiometricPrompt API

## Day 5 Outcome
Authentication module implemented. 1 PR created targeting development.