package com.mealmanager.backend.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import org.springframework.scheduling.annotation.EnableScheduling

@Configuration
@EnableScheduling
class AppConfig {

    @Bean
    fun restTemplate(): RestTemplate = RestTemplate()

    @Bean
    fun databaseConfig(): DatabaseConfig = DatabaseConfig()

    @Bean
    fun cacheConfig(): CacheConfig = CacheConfig()

    @Bean
    fun corsConfig(): CorsConfig = CorsConfig()
}

class DatabaseConfig {
    fun url(): String = "jdbc:postgresql://localhost:5432/mealmanager"
    fun username(): String = "mealmanager"
    fun password(): String = System.getenv("DB_PASSWORD") ?: "password"
    fun poolSize(): Int = 20
}

class CacheConfig {
    fun ttlSeconds(): Int = 300
    fun maxEntries(): Int = 10000
}

class CorsConfig {
    fun allowedOrigins(): List<String> = listOf("https://app.mealmanager.com", "http://localhost:3000")
    fun allowedMethods(): List<String> = listOf("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
}

class FeatureFlagConfig {
    fun refreshIntervalSeconds(): Int = 60
}

class MonitoringConfig {
    fun enabled(): Boolean = true
    fun metricsEndpoint(): String = "/api/v1/monitoring/metrics"
    fun healthEndpoint(): String = "/api/v1/monitoring/health"
}

class TracingConfig {
    fun serviceName(): String = "meal-manager-backend"
    fun sampleRate(): Double = 0.1
}

class I18nConfig {
    fun supportedLocales(): List<String> = listOf("en", "es", "fr", "de", "ja", "zh")
    fun defaultLocale(): String = "en"
}

class SecurityProperties {
    fun jwtSecret(): String = System.getenv("JWT_SECRET") ?: "dev-secret-change-me"
    fun tokenExpirationSeconds(): Int = 3600
    fun refreshExpirationSeconds(): Int = 86400 * 7
}
