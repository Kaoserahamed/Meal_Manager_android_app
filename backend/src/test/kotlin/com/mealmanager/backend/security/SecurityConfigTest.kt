package com.mealmanager.backend.security

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class SecurityConfigTest {

    @Test
    fun `jwtAuthConverter is not null`() {
        val converter = JwtAuthConverter()
        assertNotNull(converter)
    }

    @Test
    fun `jwtFilter is not null`() {
        val filter = JwtFilter()
        assertNotNull(filter)
    }

    @Test
    fun `oauth2Config has issuer`() {
        val config = OAuth2Config()
        assertNotNull(config.issuerUri())
    }

    @Test
    fun `biometricConfig enabled default`() {
        val config = BiometricConfig()
        assertTrue(config.enabled())
    }
}
